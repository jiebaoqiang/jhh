package com.jhh.dc.baika.service.app;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.app.LoanService;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.DepositDo;
import com.jhh.dc.baika.api.entity.PaymentInfoDo;
import com.jhh.dc.baika.api.entity.PaymentInfoVo;
import com.jhh.dc.baika.api.entity.RepayInfoDo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.UserNodeDo;
import com.jhh.dc.baika.api.enums.NodeEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.HttpUrlPost;
import com.jhh.dc.baika.common.util.PropertiesReaderUtil;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.entity.app.Bank;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.app_vo.SignInfo;
import com.jhh.dc.baika.entity.manager.Review;
import com.jhh.dc.baika.mapper.app.BankMapper;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.app.CodeValueMapper;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import com.jhh.dc.baika.mapper.app.ReviewersMapper;
import com.jhh.dc.baika.mapper.contract.ContractMapper;
import com.jhh.dc.baika.mapper.manager.ReviewMapper;
import com.jhh.dc.baika.mapper.product.ProductMapper;
import com.jhh.dc.baika.service.bankdeposit.QianQiDepositWebImpl;
import com.jhh.dc.baika.service.depository.DepositoryPayServiceImpl;
import com.jhh.dc.baika.util.PostAsync;
import com.jinhuhang.settlement.dto.SettlementResult;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jhh.dc.baika.common.util.CodeReturn.STATUS_TO_REPAY;


/**
 * 借款模块接口实现类
 *
 * @author xuepengfei
 *         2016年10月9日下午3:40:59
 */
@Service
public class LoanServiceImpl implements LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    //借款状态常量
    private static final String STATUS_APLLY = CodeReturn.STATUS_APLLY;//申请中
    private static final String STATUS_CANCEL = CodeReturn.STATUS_CANCEL;//已取消
    private static final String STATUS_WAIT_SIGN = CodeReturn.STATUS_WAIT_SIGN;//待签约
    private static final String STATUS_SIGNED = CodeReturn.STATUS_SIGNED;//已签约

    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private BorrowListMapper borrowListMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ReviewersMapper reviewersMapper;
    @Autowired
    private JedisCluster jedisCluster;
    @Autowired
    private CodeValueMapper codeValueMapper;
    @Autowired
    private AgreementServiceImpl agreementService;
    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private QianQiDepositWebImpl qianQiDepositWebImpl;

    @Autowired
    private DepositoryPayServiceImpl depositoryPayService;

    @Value("${A.synchrodata.borrowUrl}")
    private String borrowUrl;
    @Value("${A.synchrodata.personUrl}")
    private String personUrl;

    @Value("${A.synchrodata.contract.personUrl}")
    private String contractUrl;

    @Override
    public String getCollectionUser(int borrId) {
        BorrowList borrow = borrowListMapper.getBorrowListByBorrId(borrId);
        if (borrow != null) {
            return borrow.getCollectionUser();
        }
        return null;
    }


    /**
     * 生成借款记录
     *
     * @return
     */
    private ResponseDo<BorrowList> borrowProduct(String userId,Product product, String reviewer) {
        logger.info("生成借款记录 userId =" + userId + "\nproduct=" + product);
        ResponseDo<BorrowList> result = new ResponseDo<>();
        // 幂等操作
        if (StringUtils.isEmpty(jedisCluster.get(RedisConst.BP_KEY + userId))) {
            String setnx = jedisCluster.set(RedisConst.BP_KEY + userId, userId, "NX", "EX", 60);
            if (!"OK".equals(setnx)) {
                result.setCode(StateCode.ORDER_REPEAT_CODE);
                result.setInfo(StateCode.ORDER_REPEAT_MSG);
                logger.error("borrowProduct直接返回，重复数据per_id" + userId);
                return result;
            }
        } else {
            result.setCode(StateCode.ORDER_REPEAT_CODE);
            result.setInfo(StateCode.ORDER_REPEAT_MSG);
            logger.error("borrowProduct直接返回，重复数据per_id" + userId);
            return result;
        }
        if (product == null || "D".equals(product.getStatus())){
            return ResponseDo.newFailedDo("暂不支持该产品，请返回选择其他产品");
        }
        //新建borr时先检查是否有申请中的借款
        Integer haveBorrowing = borrowListMapper.selectDoing(Integer.parseInt(userId));
        if (haveBorrowing > 0) {//有电审未通过，已结清，已取消，已逾期结清之外的借款
            logger.error("有电审未通过，已结清，已取消，已逾期结清之外的借款");
            result.setCode(StateCode.ORDER_UNFINISHED_CODE);
            result.setInfo(StateCode.ORDER_UNFINISHED_MSG);
            return result;
        }
        Person person = personMapper.selectByPrimaryKey(Integer.parseInt(userId));
        BorrowList newBlist = borrowListMapper.selectNow(Integer.parseInt(userId));
        BorrowList bl = new BorrowList(product, person, newBlist == null ? null : newBlist.getBorrStatus());
        //保存借款信息
        borrowListMapper.insertSelective(bl);
        //创建审核人
        setReviewer(bl.getId(), reviewer);
        result.setCode(StateCode.SUCCESS_CODE);
        result.setInfo(StateCode.SUCCESS_MSG);
        result.setData(bl);
        //新建借款记录成功
        return result;
    }


    /**
     * 合同签约，状态改为已签约，添加签约时间
     *
     * @param borrId 合同id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDo<?> signingBorrow(String borrId, String loanUse) {
        //构建结果对象，默认201 失败
        //borr_id查询到用户当前的借款表
        BorrowList borrowList = borrowListMapper.selectByPrimaryKey(Integer.valueOf(borrId));

        //把当前借款表的借款状态改为已签约,添加签约时间
        if (!STATUS_WAIT_SIGN.equals(borrowList.getBorrStatus())) {
            //如果借款表的状态不为待签约，不能签约
            return ResponseDo.newFailedDo("合同状态错误，无法签约");
        }
        borrowList.setBorrStatus(STATUS_SIGNED);
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        borrowList.setMakeborrDate(date);
        borrowList.setUpdateDate(date);
        borrowList.setLoanUse(loanUse);
        int i = borrowListMapper.updateByPrimaryKeySelective(borrowList);
        if (i > 0) {
            // 保存节点
            personMapper.updateApplyNodeByPerId(borrowList.getPerId(), NodeEnum.UN_SIGN.getNode());
            //同步告知A
            AsyncExecutor.execute(new PostAsync<>(borrowList, borrowUrl, borrowListMapper));
            //更改成功
            // TODO 暂时关闭机器人审核，待百可录机器人打开后再开启此代码
            signRobot(borrowList);
        }
        return ResponseDo.newSuccessDo();
    }

    /**
     * 取消借款申请。判断合同状态，在申请中的合同才能取消借款申请。
     *
     * @param per_id  用户ID
     * @param borr_id 合同id
     * @return
     */
    @Override
    public ResponseDo<?> cancelAskBorrow(String per_id, String borr_id) {
        //构建结果对象，默认201 失败
        try {
            //根据borr_id获取借款表
            BorrowList borrowList = borrowListMapper.selectByPrimaryKey(Integer.valueOf(borr_id));
            String status = borrowList.getBorrStatus();
            if (STATUS_APLLY.equals(status) || STATUS_WAIT_SIGN.equals(status)) {
                borrowList.setBorrStatus(STATUS_CANCEL);
                long time = System.currentTimeMillis();
                Date date = new Date(time);
                borrowList.setUpdateDate(date);
                borrowListMapper.updateByPrimaryKeySelective(borrowList);
                //同步告知A
                AsyncExecutor.execute(new PostAsync<>(borrowList, borrowUrl, borrowListMapper));
                //取消借款申请成功
                return ResponseDo.newSuccessDo();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDo.newFailedDo("服务器正在开小差，请稍候");
        }
        return null;
    }


    @Override
    public SignInfo getSignInfo(String phone) {
        //获取用户信息
        Person person = personMapper.getPersonByPhone(phone);
        //获取用户当前合同
        BorrowList borrowList = borrowListMapper.selectNow(person.getId());
        BigDecimal borrAmount = new BigDecimal(borrowList.getBorrAmount()).setScale(2,BigDecimal.ROUND_HALF_UP);
        SignInfo signInfo = new SignInfo();
        signInfo.setPerId(person.getId());
        signInfo.setPhone(phone);
        signInfo.setBorrNum(borrowList.getBorrNum());
        signInfo.setBorrId(borrowList.getId());
        signInfo.setName(person.getName());
        signInfo.setBorrAmount(borrAmount);
        signInfo.setTermNum(borrowList.getTermNum());
        signInfo.setBankNum(person.getBankCard());

        // 试算到期应还
        Product product = productMapper.selectByPrimaryKey(borrowList.getProdId());
        BigDecimal planRepay = borrAmount.add(borrAmount.multiply(new BigDecimal(product.getInterestRate() + product.getServiceFee() + product.getManageFee()))).setScale(2,BigDecimal.ROUND_HALF_UP);
        signInfo.setPlanRepay(planRepay);
        signInfo.setInterestRate(borrAmount.multiply(new BigDecimal(product.getInterestRate())).setScale(2,BigDecimal.ROUND_HALF_UP));
        signInfo.setServiceFee(borrAmount.multiply(new BigDecimal(product.getServiceFee())).setScale(2,BigDecimal.ROUND_HALF_UP));
        signInfo.setManageFee(borrAmount.multiply(new BigDecimal(product.getManageFee())).setScale(2,BigDecimal.ROUND_HALF_UP));
        return signInfo;
    }


    @Override
    public ResponseDo<Map<String,Object>> applyBorrow(String phone, String productId, String borrNum, String reviewer) {
        logger.info("用户申请借款接口调用 phone=" + phone + "borrNum=" + borrNum + "reviewer=" + reviewer);
        Person person = personMapper.getPersonByPhone(phone);
        if (person == null) {
            //用户首次登陆 向A获取数据
            String md5 = ""; //TODO:Md5验证最后写
            String param = "phone=" + phone + "&md5=" + md5;
            String result = HttpUrlPost.sendGet(personUrl, param);
            if (StringUtils.isEmpty(result)) {
                return ResponseDo.newFailedDo("用户获取失败，请重新申请");
            } else {
                person = JSONObject.parseObject(result, Person.class);
                if (person == null ){
                    return ResponseDo.newFailedDo("用户获取失败，请重新申请");
                }
                person.setApplyNode(NodeEnum.UN_LOGIN_NODE.getNode());
                personMapper.insertSelective(person);
            }
        }
        //根据产品id获取产品类型
        Product product = productMapper.selectByPrimaryKey(Integer.parseInt(productId));
        //获取当前用户借款信息
        BorrowList  borrowList = borrowListMapper.selectNow(person.getId());
        try {
            //判断用户当前状态是否结清，结清则生成合同
            List<String> borrEnd = Arrays.asList(CodeReturn.STATUS_PAY_BACK,CodeReturn.STATUS_CANCEL,CodeReturn.STATUS_REVIEW_FAIL,
                    CodeReturn.STATUS_PHONE_REVIEW_FAIL,CodeReturn.STATUS_DELAY_PAYBACK,CodeReturn.STATUS_EARLY_PAYBACK);
            if (borrowList == null || borrEnd.contains(borrowList.getBorrStatus())) {
                //生成合同
                ResponseDo<BorrowList> aDo = borrowProduct(String.valueOf(person.getId()),product, reviewer);
                if (200 == aDo.getCode()) {
                    borrowList = aDo.getData();
                } else {
                    //合同创建失败 抛出错误
                    return ResponseDo.newFailedDo(aDo.getInfo());
                }
            }
            return checkNode(person,borrowList,product);
        } catch (Exception e) {
            logger.error("e",e);
            return ResponseDo.newFailedDo("服务器繁忙，清稍候");
        } finally {
            //合同生成成功回调告知A公司
            AsyncExecutor.execute(new PostAsync<>(borrowList, borrowUrl, borrowListMapper));
        }
    }

    /**
     *  查询用户节点
     * @param person
     * @param borrowList
     * @param product
     * @return
     */
    private ResponseDo<Map<String,Object>> checkNode(Person person,BorrowList borrowList, Product product){
        Map<String,Object> map = new HashMap<>();
        map.put("borrNum",borrowList.getBorrNum());
        map.put("prodtype",product.getProductTypeCode());
        map.put("prodId",borrowList.getProdId());
        map.put("node",person.getApplyNode());
        UserNodeDo nodeDo = new UserNodeDo();
        nodeDo.setBorrNum(borrowList.getBorrNum());
        nodeDo.setProdType(product.getProductTypeCode());
        nodeDo.setProdId(borrowList.getProdId());
        String applyNode = person.getApplyNode();
        nodeDo.setNode(applyNode);

        logger.info("该用户当前所处节点状态状态 node=" + person.getApplyNode());

        // 判断用户节点
        if(NodeEnum.UN_LOGIN_NODE.getNode().equals(applyNode)){
            // 登录页
            map.put("forwardUrl","forward:/form/login/"+person.getPhone());
        }else if(NodeEnum.UN_SAVE_USER_INFO.getNode().equals(applyNode)){
            // 填写个人信息页
            map.put("forwardUrl","forward:/form/userInfo/"+person.getPhone());

        }else if(NodeEnum.UN_OPEN_ACCOUNT.getNode().equals(applyNode)){
            // 开户页
            map.put("forwardUrl","forward:/account/openAccountHtml/"+person.getId());
        }else if(NodeEnum.UN_AUTH.getNode().equals(applyNode)){
            // 授权页
            map.put("forwardUrl","forward:/account/authAccountHtml/"+person.getId());
        }else if(NodeEnum.UN_QUICK_PAY.getNode().equals(applyNode)){
            // 快捷支付页
            map.put("forwardUrl","forward:/form/login/"+person.getPhone());
        }else if(NodeEnum.UN_SIGN.getNode().equals(applyNode)){
            // 签约页
            map.put("forwardUrl","forward:/form/login/"+person.getPhone());
        }else if(NodeEnum.SIGNED.getNode().equals(applyNode)){
            // 判断用户合同状态
            if(CodeReturn.STATUS_TO_REPAY.equals(borrowList.getBorrStatus())){
                // 借款详情页
                map.put("forwardUrl","forward:/form/login/"+person.getPhone());
            }

        }
        return ResponseDo.newSuccessDo(map);
    }

    @Override
    public ResponseDo<BorrowList> updateBorrowStatus(String perId, String rlStatus) {
        //获取用户当前合同
        BorrowList nowBorr = borrowListMapper.selectNow(Integer.parseInt(perId));
        logger.info("银行卡验证后用户当前合同状态 nowBorr" + nowBorr);
        if (nowBorr == null) {
            return ResponseDo.newFailedDo("你当前尚未申请，请返回申请");
        }
        //申请中变为待签约
        if (STATUS_APLLY.equals(nowBorr.getBorrStatus())) {
            // 保存节点
            personMapper.updateApplyNodeByPerId(Integer.parseInt(perId), NodeEnum.UN_SIGN.getNode());
            nowBorr.setBorrStatus(rlStatus);
            borrowListMapper.updateByPrimaryKeySelective(nowBorr);
            return ResponseDo.newSuccessDo(nowBorr);
        } else {
            return ResponseDo.newSuccessDo(nowBorr);
        }
    }

    @Override
    public ResponseDo<RepayInfoDo> jumpRepay(String perId, String borrId) {
        logger.info("用户付款页面跳转请求参数 perId = " + perId + "borrId=" + borrId);
        Person p = personMapper.selectByPrimaryKey(Integer.parseInt(perId));
        if (p == null) {
            return ResponseDo.newFailedDo("用户不存在");
        }
        BorrowList bl = borrowListMapper.selectByPrimaryKey(Integer.parseInt(borrId));
        if (bl == null || !(STATUS_TO_REPAY.equals(bl.getBorrStatus())
                || CodeReturn.STATUS_LATE_REPAY.equals(bl.getBorrStatus()))) {
            return ResponseDo.newFailedDo("当前合同状态不正确，无法还款");
        }
        //获取扣款手续费
        String fee = codeValueMapper.getMeaningByTypeCode("payment_fee", "1");
        //获取用户银行卡列表
        List<Bank> bank = bankMapper.selectAllBanks(perId);
        RepayInfoDo info = new RepayInfoDo();
        info.setBorrId(bl.getId());
        info.setPerId(bl.getPerId());
        info.setAmountSurplus(bl.getAmountSurplus());
        info.setBank(bank);
        info.setBorrNum(bl.getBorrNum());
        info.setPhone(p.getPhone());
        info.setFee(StringUtils.isEmpty(fee) ? 0L : new BigDecimal(fee).floatValue());
        return ResponseDo.newSuccessDo(info);
    }

    @Override
    public ResponseDo<DepositDo> jumpWithdrawal(String perId, String borrId) {
        logger.info("用户提现页面跳转请求参数 perId = " + perId + "borrId=" + borrId);
        Person p = personMapper.selectByPrimaryKey(Integer.parseInt(perId));
        if (p == null) {
            return ResponseDo.newFailedDo("用户不存在");
        }
        BorrowList bl = borrowListMapper.selectByPrimaryKey(Integer.parseInt(borrId));
        /*if (bl == null || !(STATUS_TO_REPAY.equals(bl.getBorrStatus())
                || CodeReturn.STATUS_LATE_REPAY.equals(bl.getBorrStatus()))) {
            return ResponseDo.newFailedDo("当前合同状态不正确，无法还款");
        }*/
        if (bl == null) {
            return ResponseDo.newFailedDo("当前合同不存在，无法提现");
        }
        if ("p".equals(bl.getBorrStatus())) {
            return ResponseDo.newFailedDo("当前有处理中的还款订单，无法提现");
        }
        //获取提现手续费
        String fee = qianQiDepositWebImpl.queryWithdrawCharge();
        //获取可用余额
        String amountSurplus = qianQiDepositWebImpl.queryUserAccountAmount(p.getPhone());
        //获取用户银行卡列表
        Bank bank = bankMapper.selectPrimayCardByPerId(perId);
        DepositDo depositDo = new DepositDo();
        depositDo.setBorrId(bl.getId());
        depositDo.setPerId(bl.getPerId());
        depositDo.setAmountSurplus(new BigDecimal(amountSurplus).floatValue());
        depositDo.setBank(bank);
        depositDo.setBorrNum(bl.getBorrNum());
        depositDo.setPhone(p.getPhone());
        depositDo.setFee(new BigDecimal(fee).floatValue());
        return ResponseDo.newSuccessDo(depositDo);
    }

    @Override
    public ResponseDo<?> synchronousBorrow(BorrowList borrowList) {
        //获取清结算响应状态
        logger.info("清结算状态改变 borrowList"+borrowList);
        borrowList = borrowListMapper.selectByBorrNum(borrowList.getBorrNum());
        AsyncExecutor.execute(new PostAsync<>(borrowList, borrowUrl, borrowListMapper));
        return ResponseDo.newSuccessDo();
    }

    @Override
    public ResponseDo<PaymentInfoDo> paymentInfo(PaymentInfoVo vo) {
        logger.info("获取用户付款页面信息 PaymentInfoVo = " + vo);
        //获取用户选定银行卡信息
        PaymentInfoDo info = bankMapper.getPaymentInfoDo(vo.getPerId(), vo.getBankId());
        //获取用户合同信息
        BorrowList borrowList = borrowListMapper.selectByPrimaryKey(vo.getBorrId());
        //获取客服电话
        String servicePhone = codeValueMapper.selectCodeByType("service_phone");
        info.setBorrId(borrowList.getId());
        info.setBorrNum(borrowList.getBorrNum());
        info.setProdId(borrowList.getProdId());
        info.setPerId(vo.getPerId());
        info.setAmountSurplus(vo.getAmountSurplus());
        info.setActualAmount(vo.getActualAmount());
        info.setServicePhone(servicePhone);
        return ResponseDo.newSuccessDo(info);
    }

    /**
     * 获取合同信息
     *
     * @param borrId
     * @return
     */
    @Override
    public Map<String, Object> getContractInfoByBorrId(Integer borrId) {
        String phone = borrowListMapper.getPersonPhoneByBorrId(borrId);
        String param = "phone=" + phone;
        String result = HttpUrlPost.sendGet(contractUrl, param);

        Date date = new Date();
        BorrowList borrowList = borrowListMapper.selectByPrimaryKey(borrId);
        Map<String, Object> map = JSONObject.parseObject(result, Map.class);
        map.put("borrNum", borrowList.getBorrNum());
        map.put("createDate", DateFormatUtils.format(borrowList.getCreationDate(), "yyyy年MM月dd日"));

        // 查看是否是悠兔白卡
        if("pay_money".equals(borrowList.getProdType())){
            //调用风控试算接口
            ResponseDo<SettlementResult> planInfo = agreementService.getPlanInfo(borrId);
            if (200 == planInfo.getCode()) {
                JSONObject obj = JSONObject.parseObject(planInfo.getData().getModel());
                map.put("planRepayDate", DateFormatUtils.format(obj.getDate("planrepay_date"), "yyyy年MM月dd日"));
                map.put("planRepay",obj.getBigDecimal("plan_repay").setScale(2, RoundingMode.DOWN).toPlainString());
                map.put("interestSum", obj.getBigDecimal("interest_sum").setScale(2,RoundingMode.DOWN).toPlainString());
                map.put("borrAmount",obj.getBigDecimal("capitalSum").setScale(2,RoundingMode.DOWN).toPlainString());
            }
            map.put("view","agreement/borrowAgreement");
            Bank bank = bankMapper.selectPrimayCardByPerId(String.valueOf(borrowList.getPerId()));
            map.put("bankNum",bank.getBankNum());
           // map.put("term",borrowList.getTermday()*borrowList.getTermNum());
            map.put("year",DateFormatUtils.format(date,"yyyy"));
            map.put("month",DateFormatUtils.format(date,"MM"));
            map.put("day",DateFormatUtils.format(date,"dd"));

        }else{
            map.put("view","agreement/saleContract");
        }
        return map;
    }

    /**
     * 通过合同id更新合同状态
     *
     * @param borrowNum
     * @param status
     * @return
     */
    @Override
    public String updateBorrowStatusByBorrowNum(String borrowNum, String status) {
        borrowListMapper.updateStatusByBorrNum(borrowNum, status);
        return "SUCCESS";
    }

    /**
     * 查看我的合同
     * @param borrId
     * @return
     */
    @Override
    public Map<String, String> getContractImageByBorrId(Integer borrId) {
        Map<String,String> map = contractMapper.selectContractByBorrId(borrId);
        if("pay_card".equals(map.get("prodType"))){
            map.put("title","销售合同");
        }else{
            map.put("title","借款协议");
        }
        return map;
    }

    /**
     * 分配审核人方法
     *
     * @param borrId
     * @param emp_num
     * @return
     */
    private int setReviewer(Integer borrId, String emp_num) {
        //幂等操作
        String redisResult = jedisCluster.get(RedisConst.REVIEW_KEY + borrId);
        logger.info(String.format("【setReviewer】jedisCluster get key: %s, result: %s, borrId: %s", RedisConst.REVIEW_KEY + borrId, redisResult, borrId));
        if (!StringUtils.isEmpty(redisResult)) {
            logger.error("直接返回，重复数据审核分配" + borrId);
            return 0;
        }

        String setNX = jedisCluster.set(RedisConst.REVIEW_KEY + borrId, borrId.toString(), "NX", "EX", 30 * 60);
        logger.info(String.format("【setReviewer】jedisCluster set key: %s, result: %s, borrId: %s", RedisConst.REVIEW_KEY + borrId, setNX, borrId));
        if (!"OK".equals(setNX)) {
            logger.error("直接返回，重复数据审核分配" + borrId);
            return 0;
        }

        //分配审核人之前先看有没有审核人 如果有 直接返回1
        if (reviewMapper.selectReview(borrId) > 0) {
            return 1;
        }
        //新增审核表记录
        Integer sum = reviewMapper.reviewSum();
        if (sum == null) {
            sum = 0;
        }
        //获得所有审核人的员工编号
        List<String> reviewerList = reviewersMapper.selectEmployNum();
        //给该borrowList设置审核人
        int turn = sum % reviewerList.size();

        Review review = new Review();
        review.setBorrId(borrId);
        review.setReviewType("1");
        //如果员工编号传空，自动分配
        if (StringUtils.isEmpty(emp_num)) {
            review.setEmployNum(reviewerList.get(turn));
        } else {
            review.setEmployNum(emp_num);
        }
        return reviewMapper.insertSelective(review);
    }

    public void signRobot(BorrowList borrowList) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        logger.info("当前首单审核时间为：" + currentHour + "时");
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String beginTime = codeValueMapper.selectCodeByType("baikelu_audit_begin_time");
        String endTime = codeValueMapper.selectCodeByType("baikelu_audit_end_time");
        Boolean flagBeginTime = Integer.valueOf(sdf.format(new Date()).toString()) >= Integer.valueOf(beginTime);
        Boolean flagEndTime = Integer.valueOf(sdf.format(new Date()).toString()) <= Integer.valueOf(endTime);
        logger.info("flagBeginTime:" + flagBeginTime + "---------flagEndTime:" + flagEndTime);
        if (flagBeginTime && flagEndTime) {
            logger.info("进入机器人首单审核");
            String url = PropertiesReaderUtil.read("third", "robotUrl");
            Map<String, String> map = new HashMap<>();
            map.put("borrId", borrowList.getId().toString());
            HttpUrlPost.sendPost(url, map);
        }
    }

    @Override
    public void synchBorrowStatus() {
        List<BorrowList> borrowList = borrowListMapper.getBorrowByFinalStatus();
        if (borrowList != null && borrowList.size() > 0) {
            borrowList.forEach(v -> AsyncExecutor.execute(new PostAsync<>(v, borrowUrl, borrowListMapper)));
        }
    }

    @Override
    public void synchBorrowStatusOverdue() {
        List<BorrowList> borrowList = borrowListMapper.getBorrowByOverdue();
        if (borrowList != null && borrowList.size() > 0) {
            borrowList.forEach(v -> AsyncExecutor.execute(new PostAsync<>(v, borrowUrl, borrowListMapper)));
        }
    }
}
