package com.jhh.dc.baika.service.capital.thridpay.ysb;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.mapper.app.BankMapper;
import com.jhh.dc.baika.mapper.app.CodeValueMapper;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import com.jhh.dc.baika.mapper.manager.RepaymentPlanMapper;
import com.jhh.dc.baika.api.app.BQSService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.BindCardVo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.AgentDeductRequest;
import com.jhh.dc.baika.api.loan.BankService;
import com.jhh.dc.baika.common.constant.PayCenterChannelConstant;
import com.jhh.dc.baika.common.constant.QuickPayBindStatusConstant;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.entity.app.Bank;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.pay.driver.pojo.*;
import com.jhh.pay.driver.service.TradeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 绑定银行卡功能实现类
 * 悠悠兔白卡的service
 *
 * @author xuepengfei
 *         2016年11月7日上午9:17:08
 */
@Service
@Transactional
public class BankServiceImpl implements BankService {

    private static final Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private BQSService bqsService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private CodeValueMapper codeValueMapper;

    @Value("${endDate}")
    private String end;

    private Map<String, Integer> channelValueMap;

    /**
     * 初始化快捷支付key-value 映射
     */
    @PostConstruct
    public void initChannelMap() {
        channelValueMap = new HashMap<>(3);
        channelValueMap.put(PayCenterChannelConstant.PAY_CHANNEL_HLB_QUICK, QuickPayBindStatusConstant.QUICK_PAY_HLB.getFlag());
        channelValueMap.put(PayCenterChannelConstant.PAY_CHANNEL_LKL_QUICK, QuickPayBindStatusConstant.QUICK_PAY_LKL.getFlag());
        channelValueMap.put(PayCenterChannelConstant.PAY_CHANNEL_HUIJU_QUICK, QuickPayBindStatusConstant.QUICK_PAY_HUIJU.getFlag());
    }


    /**
     * 1.支付中心绑卡
     */
    @Override
    @Transactional
    public ResponseDo<?> payCenterBindCard(BindCardVo vo) {
        ResponseDo<?> result = new ResponseDo(201, "系统繁忙");
        try {
            //根据per_id查询用户信息 : 姓名，身份号
            Person person = personMapper.selectByPrimaryKey(Integer.valueOf(vo.getPer_id()));
            if (!bindingBQS(vo.getPer_id(), person.getName(), person.getCardNum(), vo.getBank_num(), vo.getPhone(), vo.getTokenKey(), vo.getDevice())) {
                result.setCode(300);
                result.setInfo("白骑士认证建议拒绝");
                return result;
            }
            //----------------------------绑卡开始---------------------------------------
            // 查询快捷支付渠道
            List<String> quickPayChannels = codeValueMapper.selectQuickPayChannels();
            //查询银行卡是否存在
            Bank bank = bankMapper.selectByBankNumAndStatus(vo.getBank_num());
            if (bank != null && bank.getPerId() != Integer.parseInt(vo.getPer_id())){
                return ResponseDo.newFailedDo("该银行卡已被绑定");
            }
            if (bank != null && !quickPayChannels.contains(vo.getChannel())) {
                return new ResponseDo(230, "该银行卡已绑定");
            } else {
                BankInfo bindingBankInfo = new BankInfo();
                bindingBankInfo.setBankCard(vo.getBank_num());
                QueryResponse<BankBinVo> bankBin = tradeService.getBankBin(bindingBankInfo);
                logger.info("查询用户绑卡银行卡卡bin返回结果 bankBin = \n" + bankBin);
                if (bankBin == null || !"SUCCESS".equals(bankBin.getCode())) {
                    logger.error(bankBin == null ? "验证银行卡失败" : bankBin.getMsg());
                    return ResponseDo.newFailedDo("验证银行卡失败，请稍候再试");
                }
                //组装验卡参数
                BindRequest request = assemblyBindRequest(person, vo);
                logger.info("开始调用支付中心绑卡接口, 参数: = " + request);
                BindResponse bindResponse = tradeService.bindCard(request);
                logger.info("用户绑卡支付中心返回参数 BindResponse = " + bindResponse);
                //验证银行卡正确性
                if (verifyBindCard(bindResponse)) {
                    Map<String, Object> bankInfo = bindResponse.getExtension();
                    logger.info("绑卡成功后 用户卡牌信息 bankInfo" + bankInfo);
                    vo.setBankName(bankBin.getData().getBankName());
                    vo.setBankCode(bankBin.getData().getBankCode());
                    //绑卡
                    changeNodeAndPersonRedund(vo, person);
                    saveBankCard(vo, bindResponse.getMsg());
                    return ResponseDo.newSuccessDo();
                } else {
                    return ResponseDo.newFailedDo(bindResponse.getMsg());
                }

            }
        } catch (Exception e) {
            logger.error("绑卡接口出现异常，请稍候", e);
            return ResponseDo.newFailedDo( "绑卡失败，请稍候再试");
        }
    }

    /**
     * 还款及签约快捷支付
     *
     * @param request
     * @return
     */
    @Override
    public ResponseDo<String> bindQuickPayDuringPay(AgentDeductRequest request) {
        ResponseDo<String> noteResult = new ResponseDo<>();
        //查看是否存在快捷支付验证码渠道
        String valiCodeChannelKey = RedisConst.VALIDATE_CODE_CHANNEL + RedisConst.SEPARATOR + request.getPhone();
        String msgChannel = jedisCluster.get(valiCodeChannelKey);
        String[] valiCodeChannel = StringUtils.isNotEmpty(msgChannel)?msgChannel.split(","):null;
        if (StringUtils.isNotEmpty(msgChannel)) {
            Bank bank = bankMapper.selectByBankNumAndStatus(request.getBankNum());
            BindCardVo bindCardVo = new BindCardVo();
            if (valiCodeChannel !=null && valiCodeChannel.length > 0) {
                bindCardVo.setMsgChannel(valiCodeChannel[0]);
                bindCardVo.setChannel(valiCodeChannel[0]);
                bindCardVo.setOrderNo(valiCodeChannel[1]);
            }
            bindCardVo.setBankCode(bank.getBankCode());
            bindCardVo.setBankName(bank.getBankName());
            bindCardVo.setBank_num(bank.getBankNum());
            bindCardVo.setPhone(bank.getPhone());
            bindCardVo.setStatus(bank.getStatus());
            bindCardVo.setPer_id(String.valueOf(bank.getPerId()));
            bindCardVo.setTokenKey("");
            bindCardVo.setValidateCode(request.getValidateCode());
            bindCardVo.setExtension("{\"userId\":\"" + bank.getPerId() + "\"}");
            noteResult = bindQuickPayDuringBind(bindCardVo);
        }
        return noteResult;
    }


    /**
     * 绑卡及签约快捷支付
     *
     * @param vo
     * @return
     */
    @Override
    public ResponseDo<String> bindQuickPayDuringBind(BindCardVo vo) {
        vo.setExtension("{\"userId\":\"" + vo.getPer_id() + "\",\"orderNo\":\"" + vo.getOrderNo() + "\"}");
        ResponseDo<?> noteResult = payCenterBindCard(vo);
        if (200 !=noteResult.getCode() && 230!=noteResult.getCode()) {
            if (StringUtils.isNotEmpty(vo.getMsgChannel())) {
                logger.info("绑定" + vo.getMsgChannel() + "快捷支付失败:" + noteResult.getInfo());
            } else {
                logger.error("绑卡失败:" + noteResult.getInfo());
            }
            return ResponseDo.newFailedDo(noteResult.getInfo());
        }
        return ResponseDo.newSuccessDo(vo.getMsgChannel());
    }

    private void changeNodeAndPersonRedund(BindCardVo vo, Person person) {
        if ("1".equals(vo.getStatus())) {
            //如果本次绑定是主卡，把用户原来主卡状态改为副卡
            bankMapper.updateBankStatusWithoutBankNum("2", person.getId(), "1", vo.getBank_num());
            //更新person
            person.setBankName(vo.getBankName());
            person.setBankCard(vo.getBank_num());
            personMapper.updateByPrimaryKeySelective(person);
        }
    }

    /**
     * 验证绑卡
     *
     * @param bindResponse
     */
    private boolean verifyBindCard(BindResponse bindResponse) throws Exception {
        if (bindResponse == null) {
            throw new Exception("支付中心绑卡出现错误");
        }
        // SYS:BIND:0002 未指定绑卡渠道指定code 正常入库
        return Constants.PayStyleConstants.JHH_PAY_STATE_SUCCESS.equals(bindResponse.getState()) || "SYS:BIND:0002".equals(bindResponse.getCode());
    }

    private void saveBankCard(BindCardVo vo, String msg) throws ParseException {
        // 查询该卡在数据库是否存在
        Bank bank = bankMapper.selectByBankNumAndStatus(vo.getBank_num());
        Bank bank1 = new Bank();
        bank1.setPerId(Integer.parseInt(vo.getPer_id()));
        bank1.setBankNum(vo.getBank_num());
        bank1.setPhone(vo.getPhone());
        bank1.setBankName(vo.getBankName());
        bank1.setBankCode(vo.getBankCode());
        bank1.setStatus(vo.getStatus());
        bank1.setStartDate(new Date());
        bank1.setEndDate(new SimpleDateFormat("yyyyMMdd").parse(end));
        bank1.setResultCode("0000");
        bank1.setResultMsg(msg);
        if (bank == null) {
            bankMapper.insertSelective(bank1);
        } else {
            bank1.setId(bank.getId());
            bankMapper.updateByPrimaryKeySelective(bank1);
        }
    }

    private BindRequest assemblyBindRequest(Person person, BindCardVo vo) {
        BindRequest request = new BindRequest();
        request.setAppId(Constants.PayStyleConstants.DC_BIND_CARD_APPID);
        request.setPersonName(person.getName());
        request.setBankCard(vo.getBank_num());
        request.setBankMobile(person.getPhone());
        request.setBankName(vo.getBankName());
        request.setIdCardNo(person.getCardNum());
        request.setValidateCode(vo.getValidateCode());
        request.setIsActualBind("true");
        request.setPurpose("绑卡");
        request.setTs(String.valueOf(new Date().getTime() / 1000));
        request.setSign("12345");
        request.setExtension(vo.getExtension());
        if (vo.getMsgChannel() != null) {
            request.setChannel(vo.getMsgChannel());
            Map<String, Object> channels = new HashMap<>();
            channels.put(vo.getMsgChannel(), "");
            request.setChannels(channels);
        }
        return request;
    }



    @Override
    public boolean bindingBQS(String per_id, String name, String card_num, String bank_num, String bank_phone, String tokenKey, String device) {
        Person person = personMapper.selectByPrimaryKey(Integer.valueOf(per_id));
        return bqsService.runBQS(person.getPhone(), person.getName(), person.getCardNum(), "verify", tokenKey, device);
    }



    /**
     * 获取可以代扣及支付的银行卡列表
     *
     * @return
     */
    @Override
    public ResponseDo<BankBaseInfo[]> getBankList() {
        //    String redis = jedisCluster.get(RedisConst.BANKLIST_KEY);
        BankBaseInfo[] bankBaseInfo;
        // if (StringUtils.isEmpty(redis)) {
        QueryRequest request = new QueryRequest();
        request.setAppId(Constants.PayStyleConstants.DC_DEDUCT_APPID);
        QueryResponse<BankBaseInfo[]> bankList = tradeService.getIntersectBanksByAppId(request);
        if (bankList == null) {
            logger.error("银行卡列表获取失败");
            return ResponseDo.newFailedDo("银行卡列表获取失败");
        } else {
            logger.info("调用支付中心查询银行卡列表接口返回结果 QueryResponse<BankBaseInfo> = " + bankList.toString());
            if (Constants.PayStyleConstants.JHH_PAY_STATE_SUCCESS.equals(bankList.getCode())) {
                bankBaseInfo = bankList.getData();
                // jedisCluster.set(RedisConst.BANKLIST_KEY, JSONArray.toJSONString(BankBaseInfo));
            } else {
                return ResponseDo.newFailedDo("银行卡列表获取失败");
            }
            //       }

        } /*else {
            // redis里有
            BankBaseInfo = JSONArray.parseObject(redis,BankBaseInfo[].class);
        }*/
        return ResponseDo.newSuccessDo(bankBaseInfo);
    }

    /**
     * 扣款前验证是否可以扣款
     *
     * @param borrId
     * @return
     */
    @SuppressWarnings("Duplicates")
    private NoteResult canPayCollect(String borrId, double thisAmount) {

        NoteResult result = new NoteResult(CodeReturn.FAIL_CODE, "失败");
        try {
            // 查出应还金额减去p状态还款订单总额=剩余可还金额
            double canPay = repaymentPlanMapper.selectCanPay(borrId);
            logger.error("本次提交borrId:" + borrId + ",本次提交金额：" + thisAmount + ",剩余应还金额：" + canPay);

            // 如果本次订单金额大于剩余可还金额 不允许还款及代扣
            if (canPay == 0 || thisAmount > canPay) {
                result.setInfo("有正在处理中的还款，当前最多可以还款" + canPay + "元");
                result.setData(canPay);
                return result;
            } else {
                result.setCode(CodeReturn.SUCCESS_CODE);
                result.setData(canPay);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new NoteResult(CodeReturn.FAIL_CODE, "失败");
        }

    }


}
