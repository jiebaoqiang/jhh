package com.jhh.dc.baika.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.jhh.dc.baika.entity.manager.*;
import com.jhh.dc.baika.manage.entity.*;
import com.jhh.dc.baika.manage.mapper.*;
import com.jhh.dc.baika.manage.service.common.RedisService;
import com.jhh.dc.baika.manage.service.loan.LoanManagementService;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.AgentDeductBatchRequest;
import com.jhh.dc.baika.api.entity.capital.AgentDeductRequest;
import com.jhh.dc.baika.api.product.ProductService;
import com.jhh.dc.baika.common.constant.PayCenterChannelConstant;
import com.jhh.dc.baika.common.enums.AgentDeductResponseEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.Detect;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.entity.app.BankVo;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app.Product;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.entity.loan.CollectorsList;
import com.jhh.dc.baika.entity.loan.ReceiptUsers;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.manager_vo.LoanInfoVo;
import com.jhh.dc.baika.entity.manager_vo.LoanManagementVo;
import com.jhh.dc.baika.manage.entity.*;
import com.jhh.dc.baika.manage.forkjoin.ForkJoinTask;
import com.jhh.dc.baika.manage.mapper.*;
import com.jhh.dc.baika.manage.service.borr.BorrListService;
import com.jhh.dc.baika.manage.utils.Assertion;
import com.jhh.dc.baika.manage.utils.UrlReader;
import com.jhh.pay.driver.service.TradeService;
import com.jinhuhang.risk.dto.blacklist.jsonbean.BlackListDto;
import com.jinhuhang.risk.service.impl.blacklist.BlacklistAPIClient;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisCluster;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

@Service
@Log4j
public class LoanManagementServiceImpl implements LoanManagementService {
    private Logger logger = LoggerFactory.getLogger(LoanManagementServiceImpl.class);
    @Autowired
    private RepayPlanMapper repayPlanMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private ManagerOfLoanMapper managerOfLoanMapper;
    @Autowired
    private LoanCompanyMapper companyMapper;
    @Autowired
    private SystemUserMapper collectorsMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private RepaymentMapper repaymentMapper;
    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private CollectorsListMapper collectorsListMapper;

    @Autowired
    private CollectorsRemarkMapper collectorsRemarkMapper;

    @Autowired
    private BorrListService borrListService;

    @Autowired
    private BankInfoMapper bankInfoMapper;

    @Autowired
    private DubboTranService dubboTranService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CompanyOrderMapper companyOrderMapper;

    @Autowired
    private CodeValueMapper codeValueMapper;

    @Autowired
    private AgentChannelService agentChannelService;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private BlacklistAPIClient riskClient;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserService userService;

    private static final String RISK_SUCCESS = "1";

    @PostConstruct
    public void initParam() {
        // 初始化redis中拉卡拉支付渠道
        jedisCluster.setnx(RedisConst.PAY_CHANNEL_LKL, PayCenterChannelConstant.PAY_CHANNEL_LKL);
    }

    @Override
    public PageInfo<LoanManagementVo> selectLoanManagementInfo(HttpServletRequest request, String contractKey) {
        return selectLoanManagementInfo(request, contractKey, null);
    }

    @Override
    public PageInfo<LoanManagementVo> selectLoanManagementInfo(HttpServletRequest request, String contractKey, String from) {
        Map<String, Object> param = QueryParamUtils.getParams(request.getParameterMap());

        String userNo = StringUtils.isEmpty(request.getParameter("userNo")) ? "SYSTEM" : request.getParameter("userNo");
        addAuthLevel2queryMap(param, userNo);

        if (contractKey != null) {
            param.put("contractKey", contractKey);
        }
        if (from != null) {
            param.put("from", from);
        }
        //不需要分页查询
        QueryParamUtils.buildPage(request, false);
        List<LoanManagementVo> loanList = repaymentMapper.getLoanManagement(param);

        List<SystemUser> collectorList = collectorsMapper.selectAll();
        Map<String, String> collectorMap = new HashMap<String, String>();
        for (SystemUser collector : collectorList) {
            collectorMap.put(collector.getUserSysno(), collector.getUserName());
        }

        for (LoanManagementVo loanManagement : loanList) {
            loanManagement.setUserName(collectorMap.get(loanManagement.getUserName()));
        }


        //黑名单访问
        handleBlackList(loanList);
        //分页设置总条数
        PageInfo<LoanManagementVo> vos = new PageInfo<>(loanList);
        long totalCount = repaymentMapper.getLoanManagementCount(param);
        vos.setTotal(totalCount);

        return vos;
    }

    private void addAuthLevel2queryMap(Map<String, Object> queryMap, String userNo) {
        SystemUser c = new SystemUser();
        c.setUserSysno(userNo);
        SystemUser collectors = collectorsMapper.selectOne(c);
        queryMap.put("levelType", collectors == null ? "" : collectors.getLevelType());
        queryMap.put("companyId", collectors == null ? "" : collectors.getUserGroupId());
    }

    @Override
    public PageInfo<ReceiptUsers> selectReceiptUsers(Map<String, Object> queryMap, int offset, int size) {
        //查询贷后及外包的人员
        List<LoanCompany> companies = companyMapper.selectAll();
        String companyIds = "";
        List<String> companyNames = new ArrayList<>();
        companyNames.add("金互行-风控部");
        companyNames.add("金互行-系统管理");
        companyNames.add("金互行-运营管理部");

        for (LoanCompany company : companies) {
            if (!companyNames.contains(company.getName())) {
                companyIds += company.getId() + ",";
            }
        }
        companyIds = companyIds.substring(0, companyIds.length() - 1);
        queryMap.put("companyIds", companyIds);
        logger.info("selectReceiptUsers查询参数是:" + queryMap.toString());
        PageHelper.offsetPage(offset, size);
        List<ReceiptUsers> receiptUsers = repayPlanMapper.selectReceiptUsersNew(queryMap);
        return new PageInfo<ReceiptUsers>(receiptUsers);
    }

    @Override
    public Result askCollection(AskCollection askCollection) {
        // 清结算时间不能做还款操作
        Result result = getSettlementSwitchO();
        if (result.getCode() == Result.FAIL) {
            return result;
        }
        //查询催收人
        CollectorsList collectorsList = new CollectorsList();
        collectorsList.setContractSysno(askCollection.getBorrId());
        collectorsList.setIsDelete(2);
        collectorsList = collectorsListMapper.selectOne(collectorsList);
        String collectionUser = null;

        if (collectorsList != null && Detect.notEmpty(collectorsList.getBedueUserSysno())) {
            collectionUser = collectorsList.getBedueUserSysno();
        }

        String guid = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        logger.info("请求参数:guid:{};borrId:{};name:{};IdCardNo:{};OptAmount:{};" +
                        "BankId:{};BankNum:{};Phone:{};Description:{};CreateUser:{};collectionUser:{};payChannel:{}",
                guid,
                askCollection.getBorrId(),
                askCollection.getName(),
                askCollection.getIdCardNo(),
                askCollection.getOptAmount(),
                askCollection.getBankId(),
                askCollection.getBankNum(),
                askCollection.getPhone(),
                askCollection.getDescription(),
                askCollection.getCreateUser(),
                collectionUser,
                askCollection.getPayChannel()
        );

        AgentDeductRequest request = new AgentDeductRequest();
        request.setBankId(askCollection.getBankId());
        request.setBankCode(askCollection.getBankCode());
        request.setBankName(askCollection.getBankName());
        request.setBankNum(askCollection.getBankNum());
        request.setCollectionUser(collectionUser);
        request.setCreateUser(askCollection.getCreateUser());
        request.setDescription(askCollection.getDescription());
        request.setGuid(guid);
        request.setBorrId(askCollection.getBorrId());
        request.setIdCardNo(askCollection.getIdCardNo());
        request.setName(askCollection.getName());
        request.setOptAmount(askCollection.getOptAmount());
        request.setPhone(askCollection.getPhone());
        request.setTriggerStyle("0");
        request.setPayChannel(askCollection.getPayChannel());

        request.setType("2"); //正常结清 ，只结清当期
        if (askCollection.getBedueDays() > 7) { //提前结清
            request.setType("1");
        }

        ResponseDo noteResult = null;
        try {
            noteResult = agentChannelService.deduct(
                    request
            );
        } catch (Exception e) {
            logger.error("代扣失败", e);
            noteResult = new ResponseDo();
            noteResult.setCode(CodeReturn.fail);
        }

        //保存最新扣款时间
        saveCurrentRepayTime(Integer.valueOf(askCollection.getBorrId()));
        if (CodeReturn.success == noteResult.getCode()) {
            String orderSer = (String) noteResult.getData();
            if (StringUtils.isNotEmpty(askCollection.getCreateUser())) {

                //根据创建人查询所属公司
                SystemUser queryLevel = new SystemUser();
                queryLevel.setUserSysno(askCollection.getCreateUser());
                SystemUser level = collectorsMapper.selectOne(queryLevel);
                if (level != null) {
                    //查询受理的订单
                    Order insertOrder = orderMapper.selectBySerial(orderSer);
                    if (insertOrder != null) {
                        LoanCompanyOrder companyOrder = new LoanCompanyOrder();
                        companyOrder.setCompanyId(level.getUserGroupId());
                        companyOrder.setOrderId(insertOrder.getId());
                        companyOrder.setCreateUser(askCollection.getCreateUser());
                        companyOrder.setCreateDate(new Date());

                        int count = companyOrderMapper.insertSelective(companyOrder);
                        logger.info("插入公司流水条数{}", count);
                    }
                }

            }
            result.setCode(Result.SUCCESS);
            result.setMessage(noteResult.getInfo());
            result.setObject(noteResult.getData());
        } else {
            result.setCode(Result.FAIL);
            result.setMessage(noteResult.getInfo());
        }
        return result;
    }

    protected void saveCurrentRepayTime(Integer borrId) {
        if (Detect.isPositive(borrId)) {
            BorrowList borrowList = new BorrowList();
            borrowList.setId(borrId);
            borrowList.setCurrentRepayTime(Calendar.getInstance().getTime());
            borrowListMapper.updateByPrimaryKeySelective(borrowList);
        }
    }

    @Override
    public int queryExportCount(Map<String, Object> queryMap) {
        buildQueryCondition(queryMap);
        return repayPlanMapper.queryExportCount(queryMap);
    }

    @Override
    public List<LoanManagementVo> selectExportData(Map<String, Object> queryMap, Integer count, String userNo) {
        buildQueryCondition(queryMap);
        queryMap.put("startItem", 0);
        queryMap.put("pageSize", count == 0 ? Integer.MAX_VALUE : count);
        addAuth2queryMap(queryMap, userNo);
        List<LoanManagementVo> loanManagementVoList = repayPlanMapper.selectExportData(queryMap);
        return loanManagementVoList;
    }

    private void addAuth2queryMap(Map<String, Object> queryMap, String userNo) {
        SystemUser c = new SystemUser();
        c.setUserSysno(userNo);
        SystemUser collectors = collectorsMapper.selectOne(c);
        queryMap.put("Type", collectors == null ? "" : collectors.getLevelType());
        queryMap.put("companyId", collectors == null ? "" : collectors.getUserGroupId());
    }

    private void buildQueryCondition(Map<String, Object> queryMap) {

        if (queryMap != null) {
            String customerName = (String) queryMap.get("customerName");
            String customerIdValue = (String) queryMap.get("customerIdValue");
            String customerMobile = (String) queryMap.get("customerMobile");
            Map<String, Object> map = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(customerName)) {
                map.put("name", customerName);
            }
            if (StringUtils.isNotEmpty(customerIdValue)) {
                map.put("idCard", customerIdValue);
            }
            if (StringUtils.isNotEmpty(customerMobile)) {
                map.put("phone", customerMobile);
            }

            if (!map.isEmpty()) {
                PageHelper.offsetPage(0, 100000);
                List<Integer> perIds = personMapper.selectPersonId(map);
                if (perIds != null && perIds.size() > 0) {
                    String id = "";
                    for (int i = 0; i < perIds.size(); i++) {
                        if (i == perIds.size() - 1) {
                            id += perIds.get(i) + "";
                        } else {
                            id += perIds.get(i) + ",";
                        }
                    }
                    queryMap.put("customerId", id);
                } else {
                    queryMap.put("customerId", "-1");
                }
            }
        }
    }

    @Override
    public Download checkCanDownload() {
        int totalCount = redisService.selectDownloadCount();
        String downloadCount = UrlReader.read("max.download");
        int downloadCountInt = StringUtils.isNotEmpty(downloadCount) ? Integer.valueOf(downloadCount) : 0;
        Download download = new Download();

        download.setTotalCount(totalCount);
        download.setDownloadCount(downloadCountInt);

        return download;
    }

    @Override
    public List<Product> selectProducts() {
        ResponseDo<List<Product>> returnValue = productService.getProductAll();
        if (returnValue.getData() != null) {
            return returnValue.getData();
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<LoanInfoVo> selectLoanInfoPrivateVo(int perId) {
        List<LoanInfoVo> loanInfoVos = managerOfLoanMapper.selectLoanInfoPrivateVoForOperator(perId);
        return loanInfoVos;
    }

    @Override
    public PageInfo<SystemUser> selectReceiptUsers(Map<String, Object> queryParams, String userNo, Integer type, int offset, int size) {

        Example example = new Example(SystemUser.class);
        example.createCriteria().andEqualTo("userSysno", userNo);
        List<SystemUser> collectorList = collectorsMapper.selectByExample(example);
        if (collectorList == null || collectorList.isEmpty()) {
            return null;
        }

        SystemUser collectors = collectorList.get(0);

        if (collectors == null) {
            return null;
        }

        queryParams.put("levelType", collectors.getLevelType());
        queryParams.put("companyId", collectors.getUserGroupId());
        queryParams.put("type", type);
        PageHelper.offsetPage(offset, size);
        return new PageInfo<SystemUser>(collectorsMapper.selectDsUsers(queryParams));
    }

    @Override
    public PageInfo<SystemUser> selectReceiptUsers(String type, int offset, int size) {
        Map<String, Object> quaryParams = new HashMap<>();
        quaryParams.put("levelType", "1");
        quaryParams.put("type", type);
        PageHelper.offsetPage(offset, size);
        return new PageInfo<SystemUser>(collectorsMapper.selectDsUsers(quaryParams));
    }

    @Override
    public PageInfo<CollectorsListVo> selectCollectorsInfo(Map<String, Object> queryMap, int offset, int size, String userNo) {
        buildQueryCondition(queryMap);
        logger.info("=================>>>>>>>>>>>>>>>>催收信息查询---起始页：{}；查询参数是:{}", offset, queryMap.toString());
        Long totalCount = null;
        try {
            if (offset == 0) {
                //查询有变化,查询符合条件的count//查询参数为
                if (queryMap.isEmpty()) {
                    totalCount = redisService.selectQueryTotalItem(Constants.TYPE_COLL_INFO, "coll");
                } else {
                    totalCount = buildCollectorsInfoCount(queryMap, userNo);
                }
            } else {
                //无变化,直接取得缓存中的总条数
                totalCount = buildCollectorsInfoCount(queryMap, userNo);
            }
        } catch (Exception e) {
            //未查询到总条数，按参数查询去查
            totalCount = repayPlanMapper.selectCollectorsInfoItem(queryMap);
        }

        queryMap.put("startItem", offset);
        queryMap.put("pageSize", size);

        List<CollectorsListVo> collList = repayPlanMapper.selectCollectorsInfo(queryMap);
        PageInfo<CollectorsListVo> vos = new PageInfo<>(collList);
        vos.setTotal(totalCount);

        return vos;
    }

    private Long buildCollectorsInfoCount(Map<String, Object> queryMap, String userNo) {
        Long totalCount = null;
        if (queryMap.isEmpty()) {
            totalCount = redisService.selectQueryTotalItem(Constants.TYPE_COLL_INFO, "coll");
        } else if (queryMap.size() == 1) {
            //只有一个查询条件
            if (queryMap.containsKey("productName")) {
                //按产品类型查询
                String proId = (String) queryMap.get("productName");
                if (StringUtils.isNotEmpty(proId)) {
                    totalCount = redisService.selectQueryTotalItem(Constants.TYPE_COLL_INFO, "coll_pro_" + proId);
                } else {
                    totalCount = repayPlanMapper.selectCollectorsInfoItem(queryMap);
                    redisService.saveQueryTotalItem(Constants.TYPE_COLL_INFO, userNo, totalCount);
                }
            } else {
                totalCount = redisService.selectQueryTotalItem(Constants.TYPE_COLL_INFO, userNo);
                if (totalCount == null || totalCount == 0) {
                    totalCount = repayPlanMapper.selectCollectorsInfoItem(queryMap);
                    redisService.saveQueryTotalItem(Constants.TYPE_COLL_INFO, userNo, totalCount);
                }
            }
        } else {
            //多个查询条件,先从redis中取得条数
            totalCount = redisService.selectQueryTotalItem(Constants.TYPE_COLL_INFO, userNo);
            if (totalCount == null || totalCount == 0) {
                totalCount = repayPlanMapper.selectCollectorsInfoItem(queryMap);
                redisService.saveQueryTotalItem(Constants.TYPE_COLL_INFO, userNo, totalCount);
            }
        }

        if (totalCount == null) {
            throw new RuntimeException("总条数查询失败!");
        }

        return totalCount;
    }

    @Override
    public int addCollectionRemark(CollectorsRemark remark) {
        if (remark == null) {
            return 0;
        }
        Integer contractSysno = remark.getContractSysno();

        remark.setUpdateUser(remark.getCreateUser());

        //查询催收人并将原有操作人赋值给UpdateUser
        CollectorsList collectorsList = new CollectorsList();
        collectorsList.setContractSysno(String.valueOf(contractSysno));
        collectorsList.setIsDelete(2);
        collectorsList = collectorsListMapper.selectOne(collectorsList);
        if (collectorsList == null) {
            logger.info("添加催收备注:催收人查询失败");
            //return 0;
        }

        logger.info("添加催收备注:" + remark.toString());

        int result = collectorsRemarkMapper.insert(remark);
        if (result == 1 && contractSysno != null) {
            BorrowList borrowList = borrowListMapper.selectByPrimaryKey(contractSysno);
            if (borrowList != null) {
                borrowList.setCurrentCollectionTime(remark.getCreateDate());
                borrowListMapper.updateByPrimaryKey(borrowList);
            }
        }
        return result;
    }

    /**
     * 分页查询催收队列
     *
     * @param queryMap 查询参数
     * @return
     */
    @Override
    public PageInfo<CollectorsListVo> selectCollectorsListVo(Map<String, Object> queryMap) {
        buildQueryCondition(queryMap);
        logger.info("selectCollectorsListVo参数:" + queryMap.toString());
        List<CollectorsListVo> collectors = repayPlanMapper.selectCollectorsListInfo(queryMap);
        PageInfo<CollectorsListVo> pageInfo = new PageInfo<>(collectors);
        pageInfo.setList(pageInfo.getList());
        return pageInfo;
    }

    @Override
    public Response transferLoan(String contractIds, String userId, String opUserId) {
        Assertion.notEmpty(contractIds, "合同不能为空");
        List<String> ids = new ArrayList<>(Arrays.asList(contractIds.split(",")));
        return borrListService.saveTransferBorrList(ids, userId, opUserId);
    }

    @Override
    public BankVo selectMainBankByUserId(Integer userId) {
        logger.info("查询个人主卡:" + userId);
        return bankInfoMapper.selectMainBankByUserId(userId);
    }

    private Result getSettlementSwitchO() {
        Result result = new Result();
        result.setCode(Result.SUCCESS);
        String key = redisService.getRedisByKey(Constants.SETTLEMENT_SWITCH);
        if (Detect.notEmpty(key)) {
            if (key.equals("off")) {
                result.setCode(Result.FAIL);
                result.setMessage("清结算期间不允许扣款一类操作");
            }
        }
        return result;
    }


    private Result reduceLoanCalculate(BorrowList borrowList, String money, String type) {
        Result result = new Result();
        result.setCode(Result.SUCCESS);
        if (!Detect.isPositive(borrowList.getId())) {
            result.setCode(Result.FAIL);
            result.setMessage("合同号为空");
        } else if (!Detect.notEmpty(money)) {
            result.setCode(Result.FAIL);
            result.setMessage("金额为空");
        } else if (!Detect.notEmpty(type)) {
            result.setCode(Result.FAIL);
            result.setMessage("操作类型为空");
        }

        if (type.equals(Constants.OrderType.OFFLINE_REPAYMENT)) {
            // 由于后台管理人员可能针对线下还款施行，提前结清，则这边的判断逻辑直接去掉
//            if (Float.valueOf(money) > borrowList.getSurplusAmount()) {
//                result.setCode(Result.FAIL);
//                result.setMessage("还款金额不能大于剩余还款金额，剩余还款金额为：" + borrowList.getSurplusAmount() + "元");
//            }
        } else if(type.equals(Constants.OrderType.MITIGATE_PUNISHMENT)){
            LoanManagementVo loanManagementVo = new LoanManagementVo();
            //已还金额
            loanManagementVo.setAmountRepay(borrowList.getAmountRepay() + "");
            //已减免总额
            loanManagementVo.setReduceAmount(borrowList.getActReduceAmount() + "");
            //放款金额
            loanManagementVo.setLoanAmount(borrowList.getBorrAmount() + "");
            //剩余还款
            loanManagementVo.setSurplusTotalAmount(borrowList.getAmountSurplus() + "");

            Map map = getMaxReduceAmount(loanManagementVo);
            if(map != null || map.get("code").equals(0)){
                float reduceAmount = Float.valueOf(map.get("data") + "");

                if (Float.valueOf(money) > reduceAmount) {
                    result.setCode(Result.FAIL);
                    result.setMessage("减免金额不能大于最大减免金额，最大减免金额为：" + reduceAmount + "元");
                }
            }
        }
        return result;
    }

    @Override
    public Map getMaxReduceAmount(LoanManagementVo loanManagementVo){
        Map<String, Object> result = new HashMap<>(16);
        if(loanManagementVo == null){
            result.put("code", -100);
            result.put("info", "查询失败，合同不存在");
            return result;
        }

        Float reduceAmount = 0f;

        //已还金额
        Float amountRepay = Float.valueOf(loanManagementVo.getAmountRepay());
        //已减免总额
        Float yetReduceAmount = Float.valueOf(loanManagementVo.getReduceAmount());
        //放款金额
        Float loanAmount = Float.valueOf(loanManagementVo.getLoanAmount());
        //剩余还款
        Float surplusTotalAmount = Float.valueOf(loanManagementVo.getSurplusTotalAmount());

        if(amountRepay - yetReduceAmount >= loanAmount){
            //如果 已还金额-已减免总额 >=放款金额，【最大减免】= 剩余还款；
            reduceAmount = surplusTotalAmount;
        }else{
            //如果 已还金额-已减免总额 < 放款金额 【最大减免】= 剩余还款-放款金额+已还金额-已减免总额（负数则显示0）
            reduceAmount = surplusTotalAmount - loanAmount + amountRepay - yetReduceAmount;
            if(reduceAmount < 0){
                reduceAmount = 0f;
            }
        }
        result.put("code", 0);
        result.put("info", String.format("查询成功，contractId【%s】, bedueDays【%s】, reduceAmount【%s】", loanManagementVo.getContractID(), loanManagementVo.getBedueDays(), reduceAmount));
        result.put("data", String.format("%.2f", reduceAmount));
        return result;
    }

    @Override
    public Result reduceLoan(String contractId, String reduce, String remark, String type, String userName, String bedueDays) {

        // 清结算时间不能做还款操作
        Result result = getSettlementSwitchO();
        if (result.getCode() == Result.FAIL) {
            return result;
        }

        //根据合同号查询个人
        BorrowList b = new BorrowList();
        b.setBorrNum(contractId);
        BorrowList borrowList = borrowListMapper.selectOne(b);
        if (borrowList == null) {
            result.setCode(Result.FAIL);
            result.setMessage("合同不存在");
            return result;
        }
        result = reduceLoanCalculate(borrowList, reduce, type);
        if (result.getCode() == Result.FAIL) {
            return result;
        }

        //加入redis锁 防止重复提交
        if (!"OK".equals(jedisCluster.set(RedisConst.PAY_ORDER_KEY + borrowList.getId(), "off", "NX", "EX", 3 * 60))) {
            return new Result(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode(), "当前有还款在处理中，请稍后");
        }
        try {
            Result returnVal = dubboTranService.callDubbo(borrowList, reduce, remark, type, userName, bedueDays);
            return returnVal;
        } catch (Exception e) {
            log.error("减免异常", e);
        } finally {
            jedisCluster.del(RedisConst.PAY_ORDER_KEY + borrowList.getId());
        }
        return null;
    }

    @Override
    public List queryBatchReduce(Map<String, String[]> conditions) {
        Map param = QueryParamUtils.getParams(conditions);
        return repaymentMapper.getBatchReduce(param);
    }

    @Override
    public List getBatchReduceList(Map<String, String[]> conditions) {
        Map param = QueryParamUtils.getargs(conditions);
        List<Map> result = repaymentMapper.getBatchReduceList(param);
        result.forEach(map -> {
            map.put("createUserName", redisService.selectCollertorsUserName(map.get("createUser") + ""));
        });
        return result;
    }

    @Override
    public Result batchCollection(List<LoanManagementVo> askCollections, String reduceMoney, String createUser, String deductionsType, String payChannel) {
        // 清结算时间不能做还款操作
        Result result = getSettlementSwitchO();
        if (result.getCode() == Result.FAIL) {
            return result;
        }
        if (!"OK".equals(jedisCluster.set(RedisConst.BATCHDEDUCT_LOCK, "off", "NX", "EX", 60))) {
            logger.error("批量代扣点击频繁 锁定 ，时常1分钟 ");
            result.setCode(201);
            result.setMessage("当前已有任务正在提交，请稍候再试");
            return result;
        }
        // 初始化扣款请求list
        List<AgentDeductRequest> agentDeductRequests = new ArrayList<>();
        try {
            // 遍历数据list
            for (LoanManagementVo loanManagementVo : askCollections) {
                if (!"逾期未还".equals(loanManagementVo.getStateString())) {
                    continue;
                }
                AgentDeductRequest request = new AgentDeductRequest();
                request.setGuid(UUID.randomUUID().toString());
                request.setBorrId(loanManagementVo.getContractKey());
                request.setDescription("批量代扣");
                request.setIdCardNo(loanManagementVo.getCustomerIdValue());
                request.setName(loanManagementVo.getCustomerName());
                request.setPhone(loanManagementVo.getCustomerMobile());
                request.setCreateUser(createUser);
                request.setTriggerStyle("0");
                request.setType("2"); //正常结清 ，只结清当期
//            if (askCollection.getBedueDays() > 7) { //提前结清
//                request.setType("1");
//            }
                // 设置扣款渠道为拉卡拉
                request.setPayChannel(jedisCluster.get(RedisConst.PAY_CHANNEL_LKL));

                BankVo bankVo = bankInfoMapper.selectMainBankByUserId(Integer.valueOf(loanManagementVo.getCustomerId()));

                if (bankVo == null) {
                    logger.error("用户" + loanManagementVo.getCustomerName() + "，CustomerId = " + loanManagementVo.getCustomerId() + " 无法获取主卡信息!");
                    continue;
                }

                request.setBankId(String.valueOf(bankVo.getBankId()));
                request.setBankNum(bankVo.getBankNum());
                request.setBankName(bankVo.getBankName());

                if (!"0".equals(reduceMoney) && !"1".equals(deductionsType)) {
                    // 定额扣款
                    request.setOptAmount(reduceMoney);
                } else {
                    // 全额扣款，查询逾期应还金额
                    String mstRepayAmount = borrowListMapper.getMstRepayAmount(Integer.parseInt(loanManagementVo.getContractKey()));
                    request.setOptAmount(mstRepayAmount);
                }

                // 设置扣款类型为：定额/全额
                request.setDeductionsType(deductionsType);

                CollectorsList collectorsList = new CollectorsList();
                collectorsList.setContractSysno(loanManagementVo.getContractKey());
                collectorsList.setIsDelete(2);
                collectorsList = collectorsListMapper.selectOne(collectorsList);
                if (collectorsList != null && Detect.notEmpty(collectorsList.getBedueUserSysno())) {
                    request.setCollectionUser(collectorsList.getBedueUserSysno());
                }

                agentDeductRequests.add(request);
            }

            if (agentDeductRequests.size() < 1) {
                result.setCode(Result.SUCCESS);
                result.setMessage(String.format("成功处理订单条数为：%s条，请稍后查看扣款结果。", agentDeductRequests.size()));
                return result;
            }

            ResponseDo noteResult;
            AgentDeductBatchRequest batchCollects = new AgentDeductBatchRequest();
            // 设置支付渠道
            batchCollects.setPayChannel(payChannel);
            batchCollects.setDeductSize(agentDeductRequests.size());
            batchCollects.setRequests(agentDeductRequests);
            batchCollects.setOptPerson(createUser);
            batchCollects.setTriggerStyle("0");

            try {
                noteResult = agentChannelService.deductBatch(batchCollects);
            } catch (Exception e) {
                logger.error("批量代扣失败", e);
                result.setCode(Result.FAIL);
                result.setMessage("批量代扣失败");
                return result;
            }

            if (200 != noteResult.getCode()) {
                result.setCode(Result.FAIL);
                result.setMessage(noteResult.getInfo());
                return result;
            }

            result.setCode(Result.SUCCESS);
            result.setMessage(String.format("成功处理订单条数为：%s条，请稍后查看扣款结果。", agentDeductRequests.size()));
            result.setObject(noteResult.getData());
            return result;
        } catch (Exception e) {
            log.error(e);
            return new Result(Result.FAIL, "系统繁忙，请稍候再试");
        } finally {
            jedisCluster.del(RedisConst.BATCHDEDUCT_LOCK);
        }
    }

    /**
     * 查询扣款渠道
     *
     * @return
     */
    @Override
    public List<CodeValue> selectPayChannels() {
        List<CodeValue> payChannels = codeValueMapper.getEnableCodeValueListByCodeType("pay_center_channel");
        // 过滤支付宝渠道
        payChannels = payChannels.stream().filter(channel -> !PayCenterChannelConstant.PAY_CHANNEL_ZFB.equals(channel.getCodeCode())).collect(java.util.stream.Collectors.toList());
        return payChannels;
    }

    /**
     * 批量查询黑名单列表数据
     *
     * @param list 贷款信息列表
     */
    private List handleBlackList(List<LoanManagementVo> list) {
        // 2018-4-13 风控黑名单新接口，批量操作
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }

        List<BlackListDto> blackListDtos = Lists.newArrayList();

        list.forEach(loanManagementVo -> {
            BlackListDto dto = new BlackListDto();
            if (StringUtils.isNotBlank(loanManagementVo.getCustomerIdValue())
                    || StringUtils.isNotBlank(loanManagementVo.getCustomerMobile())) {

                dto.setIdcard(loanManagementVo.getCustomerIdValue());
                dto.setPhone(loanManagementVo.getCustomerMobile());

                blackListDtos.add(dto);
            }

        });

        if (CollectionUtils.isEmpty(blackListDtos)) {
            return list;
        }

        // 用ForkJoinTask 批量查询黑名单，每个任务大小为20000
        List blackList = new ForkJoinPool(2).invoke(new ForkJoinTask(blackListDtos));
        return getList(list, blackList);
    }

    private static List<LoanManagementVo> getList(List<LoanManagementVo> list, List blackList) {
        list.forEach(loanManagementVo -> {
            if (blackList.contains(loanManagementVo.getCustomerIdValue()) || blackList.contains(loanManagementVo.getCustomerMobile())) {
                loanManagementVo.setBlackList("Y");
            } else {
                loanManagementVo.setBlackList("N");
            }
        });
        return list;
    }

    @Override
    public List<LoansRemarkOutVo> selectExportLoansRemarkForOutWorkers(Map<String, Object> queryMap, String userNo) {
        buildQueryCondition(queryMap);
        addAuthLevel2queryMap(queryMap, userNo);
        List<LoansRemarkOutVo> outVoList = repayPlanMapper.selectExportDataForOutWorkers(queryMap);
        return outVoList;
    }

    @Override
    public List<LoansRemarkVo> selectExportLoansRemarkVo(Map<String, Object> queryMap, String userNo) {
        buildQueryCondition(queryMap);
        addAuthLevel2queryMap(queryMap, userNo);
        List<LoansRemarkVo> remarkVos = repayPlanMapper.selectExportLoansRemarkVo(queryMap);
        return remarkVos;
    }
}
