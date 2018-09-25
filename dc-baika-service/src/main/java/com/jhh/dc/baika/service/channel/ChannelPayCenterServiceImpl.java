package com.jhh.dc.baika.service.channel;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.jhh.dc.baika.api.entity.capital.*;
import com.jhh.dc.baika.api.loan.CompanyAService;
import com.jhh.dc.baika.common.util.Detect;
import com.jhh.dc.baika.entity.BaiKaFile;
import com.jhh.dc.baika.entity.BkAssetModel;
import com.jhh.dc.baika.entity.PersonInfoDto;
import com.jhh.dc.baika.entity.app.*;
import com.jhh.dc.baika.entity.app_vo.RabbitMessage;
import com.jhh.dc.baika.entity.manager_vo.PrivateVo;
import com.jhh.dc.baika.mapper.app.*;
import com.jhh.dc.baika.mapper.gen.LoanOrderDOMapper;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDO;
import com.jhh.dc.baika.mapper.loan.PayChannelAdapterMapper;
import com.jhh.dc.baika.service.capital.BasePayServiceImpl;
import com.jhh.dc.baika.util.JacksonUtil;
import com.jhh.dc.baika.util.PostAsync;
import com.jhh.dc.baika.api.channel.AgentBatchStateService;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.channel.TradeBatchStateService;
import com.jhh.dc.baika.api.channel.TradePayService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.common.enums.AgentDeductResponseEnum;
import com.jhh.dc.baika.common.enums.AgentpayResultEnum;
import com.jhh.dc.baika.common.enums.PayTriggerStyleEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.entity.callback.LKLBatchCallback;
import com.jhh.pay.driver.pojo.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 2018/3/30.
 */
@Service
@Slf4j
public class ChannelPayCenterServiceImpl extends BasePayServiceImpl implements AgentChannelService, AgentBatchStateService {

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private CodeValueMapper codeValueMapper;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private PersonMapper personMapper;
    @Autowired
    PersonInfoMapper personInfoMapper;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private PayChannelAdapterMapper payChannelAdapterMapper;

    @Autowired
    CompanyAService companyAService;

    @Resource(name = "tradePayService")
    private TradePayService tradeLocalService;

    @Value("${isTest}")
    private String isTest;

    @Value("${batchDeductSize}")
    private String batchDeductSize;

    @Value("${A.synchrodata.borrowUrl}")
    private String borrowUrl;

    @Value("${riskReportApiUrl}")
    private String riskReportApiUrl;

    @Value("${polyXinliUrl}")
    private String polyXinliUrl;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private TradeBatchStateService tradeBatchStateService;

    @Override
    public ResponseDo<?> pay(AgentPayRequest pay){
        log.info("\n[代付开始] -->走支付中心渠道 pay" + pay);
        // 幂等性操作 防止重复放款
//         if (CodeReturn.PRODUCT_TYPE_CODE_CARD.equals(pay.getProdType())){
//            return payCard(pay);
//        }
        ResponseDo responseDo = formLock(pay.getBorrId());
        if (CodeReturn.success != responseDo.getCode()) {
            return responseDo;
        }
        try {
            if (checkAgentPayLog(pay.getBorrId(), pay.getUserId())) {
                // 获取合同信息并更改合同状态
                PersonInfoDto dto = getPersonInfo(pay.getBorrId());
                // 合同状态改为放款中
                dto.getBorrowList().setBorrStatus(CodeReturn.STATUS_COM_PAYING);
                dto.getBorrowList().setUpdateDate(new Date());
                borrowListMapper.updateByPrimaryKeySelective(dto.getBorrowList());
                AsyncExecutor.execute(new PostAsync<>(dto.getBorrowList(), borrowUrl,borrowListMapper));
                // 生成流水号
                LoanOrderDO loanOrder = savePayLoanOrder(dto, Constants.payOrderType.BAIKA_PAY_TYPE, pay.getTriggerStyle(), Constants.PayStyleConstants.PAY_BK_CODE_VALUE);

                //手续费
                String fee = codeValueMapper.getMeaningByTypeCode("payment_fee", "2");
                if(Detect.notEmpty(fee)){
                    saveFeeOrder(loanOrder, fee);
                }

                BkAssetModel pushBaikaData = getPayBaiKaData(dto);
                if(null != pushBaikaData){
                    //推送白卡代付数据
                    RabbitMessage rabbitMessage = new RabbitMessage();
                    rabbitMessage.setType(2); //1:注册推送 2：资产风控推送
                    rabbitMessage.setData(pushBaikaData);
                    rabbitTemplate.convertAndSend("baikaDataQueue", JacksonUtil.objWriteStr(rabbitMessage, JsonSerialize.Inclusion.NON_EMPTY));
                    log.info("推送放款信息:" + JSONObject.toJSONString(rabbitMessage));
                    return ResponseDo.newSuccessDo();
                }
//                Bank bank = bankMapper.selectPrimayCardByPerId(String.valueOf(pay.getUserId()));
                //发起代付
//                TradeVo vo = new TradeVo(pay.getUserId(), loanOrder.getSerialNo(),
//                        pay.getPayChannel(), pay.getTriggerStyle(), bank.getBankNum(), loanOrder.getActAmount().floatValue());
//                return tradeLocalService.postPayment(vo);
                return ResponseDo.newFailedDo(AgentpayResultEnum.BORROW_INFO_ERROR.getDesc());
            } else {
                log.info("\n[代付] 此order已经有一笔单子在处理中");
                return ResponseDo.newFailedDo(AgentpayResultEnum.HAD_PROCESSING.getDesc());
            }
        } catch (Exception e) {
            log.error("支付中心代付出现异常", e);
            return ResponseDo.newFailedDo("系统繁忙");
        } finally {
            jedisCluster.del(RedisConst.PAYCONT_KEY + pay.getBorrId());
        }
    }


    @Override
    public ResponseDo state(String serNO) throws Exception {
        log.info("-------------支付中心查询订单号 " + serNO);
        //加入redis锁 防止重复提交
        if (!"OK".equals(jedisCluster.set(RedisConst.PAY_ORDER_KEY + serNO, "off", "NX", "EX", 3 * 60))) {
            return new ResponseDo<>(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode(), "当前有还款在处理中，请稍后");
        }
        if (!"OK".equals(jedisCluster.set(RedisConst.PAY_REFUND_KEY + serNO, "off", "NX", "EX", 3 * 60))) {
            return new ResponseDo<>(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode(), "当前有退款在处理中，请稍后");
        }
        try {
            ResponseDo<?> state = tradeLocalService.state(serNO);
            if (state != null) {
                afterStateHandle(state, serNO);
                return state;
            } else {
                return ResponseDo.newFailedDo("查询失败，请稍候再试");
            }
        }catch (Exception e) {
            log.info("出错：" + e.getMessage(), e);
            return new ResponseDo<>(204, "处理中,请稍候");
        } finally {
            jedisCluster.del(RedisConst.PAY_ORDER_KEY + serNO);
            jedisCluster.del(RedisConst.PAY_REFUND_KEY + serNO);
        }
    }

    @Override
    public ResponseDo batchState(List<String> loanOrder) throws Exception {
        log.info("-------------支付中心查询订单号 loanOrder = " + loanOrder.toString());
        //将list切割
        List<List<String>> partition = Lists.partition(loanOrder, Integer.parseInt(batchDeductSize));
        if (partition.size() < 1) {
            return ResponseDo.newSuccessDo();
        }
        partition.forEach(v -> {
            //防止重复提交
            lock(v);
            if (v.size() < 1) {
                return;
            }
            try {
                ResponseDo<PayResponse> result = tradeBatchStateService.batchState(v);
                if (!(result != null && Constants.CommonPayResponseConstants.SUCCESS_CODE == result.getCode())) {
                    return;
                }
                PayResponse resp = result.getData();
                if (Constants.PayStyleConstants.JHH_PAY_STATE_PROGRESSING.equals(resp.getState())
                        || Constants.PayStyleConstants.JHH_PAY_STATE_SUCCESS.equals(resp.getState())) {
                    resp.getSimpleOrders().forEach(f -> {
                        LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(f.getOrderNo());
                        if (StringUtils.isEmpty(loanOrderDO.getSid()) || StringUtils.isEmpty(loanOrderDO.getChannel())) {
                            loanOrderDO.setSid(f.getSid());
                            String channel = payChannelAdapterMapper.getChannelBypayCenterAndType(f.getChannelKey(), loanOrderDO.getType());
                            if (com.alibaba.dubbo.common.utils.StringUtils.isNotEmpty(channel)){
                                loanOrderDO.setChannel(channel);
                            }else {
                                loanOrderDO.setChannel(f.getChannelKey());
                            }
                            loanOrderDO.setChannel(f.getChannelKey());
                            loanOrderDOMapper.updateByPrimaryKeySelective(loanOrderDO);
                        }
                        if (Constants.PayStyleConstants.JHH_PAY_STATE_ERROR.equals(f.getState())
                                || Constants.PayStyleConstants.JHH_PAY_STATE_FAIL.equals(f.getState())) {
                            handleFail(loanOrderDO, f.getMsg());
                        } else if (Constants.PayStyleConstants.JHH_PAY_STATE_SUCCESS.equals(f.getState())) {
                            handleSuccess(loanOrderDO);
                        }
                    });
                }
            } catch (Exception e) {
                log.error("", e);
            } finally {
                unlock(v);
            }
        });
        return null;
    }

    /**
     * 清除锁
     *
     * @param serialNos
     */
    private void unlock(List<String> serialNos) {
        serialNos.forEach(v -> jedisCluster.del(RedisConst.PAY_ORDER_KEY + v));
    }

    /**
     * 上锁 如存在 则提剔除
     *
     * @param serialNos
     */
    private void lock(List<String> serialNos) {
        Iterator<String> iterator = serialNos.iterator();
        while (iterator.hasNext()) {
            if (!"OK".equals(jedisCluster.set(RedisConst.PAY_ORDER_KEY + iterator.next(), "off", "NX", "EX", 3 * 60))) {
                iterator.remove();
            }
        }
    }

    @Override
    public void batchCallback(LKLBatchCallback batchCallback) {
        //处理部分失败订单
        if (batchCallback != null && batchCallback.getExtension() != null && batchCallback.getExtension().get("exceptionMaps") != null) {
            Map<String,String> failOrder = (Map<String, String>) batchCallback.getExtension().get("exceptionMaps");
            log.info("批量代扣回调解析参数\n"+failOrder);
            failOrder.forEach((k, v) -> {
                LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(k);
                updateOrderForFail(loanOrderDO, v);
            });
        }
    }

    @Override
    public ResponseDo<?> deduct(AgentDeductRequest request) {
        log.info("[代扣开始] -->走支付中心渠道{} ", request);
        ResponseDo<String> result;
        //请结算锁
        result = settleLock(request.getTriggerStyle());
        if (AgentDeductResponseEnum.SUCCESS_CODE.getCode() != result.getCode()) {
            return result;
        }
        //加入redis锁 防止重复提交
        if (!"OK".equals(jedisCluster.set(RedisConst.PAY_ORDER_KEY + request.getBorrId(), "off", "NX", "EX", 3 * 60))) {
            return new ResponseDo<>(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode(), "当前有还款在处理中，请稍后");
        }
        try {
            ResponseDo<TradeVo> deductResult = doDeduct(request, "deduct");
            if (Constants.CommonPayResponseConstants.SUCCESS_CODE == deductResult.getCode()) {
                result =  tradeLocalService.postDeduct(deductResult.getData());
                return result;
            } else {
                return deductResult;
            }
        } catch (Exception e) {
            log.error("出错：" + e.getMessage(), e);
            result.setCode(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode());
            result.setInfo(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getMsg());
            return result;
        } finally {
            jedisCluster.del(RedisConst.PAY_ORDER_KEY + request.getBorrId());
        }
    }


    @Override
    public ResponseDo<?> deductBatch(AgentDeductBatchRequest requests) {
        log.info("[批量代扣开始] -->走支付中心渠道{} ", requests);
        if (requests == null) {
            return ResponseDo.newFailedDo("未获取需要批量的记录");
        }
        //清结算锁
        ResponseDo<?> result = settleLock(requests.getTriggerStyle());
        if (AgentDeductResponseEnum.SUCCESS_CODE.getCode() != result.getCode()) {
            return result;
        }
        //循环保存订单
        List<TradeVo> deduct = new ArrayList<>();
        for (AgentDeductRequest request : requests.getRequests()) {
            //加入redis锁 防止重复提交
            if (!"OK".equals(jedisCluster.set(RedisConst.PAY_ORDER_KEY + request.getBorrId(), "off", "NX", "EX", 3 * 60))) {
                log.error("批量代扣订单中用正在处理的订单 borrNum = " + request.getBorrId());
                continue;
            }
            try {
                ResponseDo<TradeVo> deductBatch = doDeduct(request, "deductBatch");
                if (Constants.CommonPayResponseConstants.SUCCESS_CODE == deductBatch.getCode()) {
                    deduct.add(deductBatch.getData());
                }
            } catch (Exception e) {
                log.error("出错：" + e.getMessage(), e);
                result.setCode(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode());
                result.setInfo(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getMsg());
            } finally {
                jedisCluster.del(RedisConst.PAY_ORDER_KEY + request.getBorrId());
            }
        }
        if (deduct.size() > 0) {
            TradeBatchVo tradeBatchVo = new TradeBatchVo(deduct, deduct.size(), requests.getPayChannel(), requests.getOptPerson());
            return tradeLocalService.batchDeduct(tradeBatchVo);
        } else {
            return ResponseDo.newFailedDo("没有可执行的订单");
        }
    }

    /**
     * @param request
     * @param deductType
     * @return
     */
    private ResponseDo<TradeVo> doDeduct(AgentDeductRequest request, String deductType) {

        BorrowList borrow = borrowListMapper.getBorrowListByBorrId(Integer.parseInt(request.getBorrId()));
        //更新代扣时间
        borrow.setCurrentRepayTime(new Date());
        borrowListMapper.updateByPrimaryKeySelective(borrow);
        //查询用户
        Person person = personMapper.selectByPrimaryKey(borrow.getPerId());
        //修改request参数
        ResponseDo<?> responseDo = updateAgentDeductRequest(request, borrow, person);
        if (!AgentDeductResponseEnum.SUCCESS_CODE.getCode().equals(responseDo.getCode())) {
            return ResponseDo.newFailedDo(responseDo.getInfo());
        }
        //提前结清，正常结算这俩中类型做金额判断
        NoteResult canPay = canPayCollect(borrow, Double.parseDouble(request.getOptAmount()));
        if (!CodeReturn.SUCCESS_CODE.equals(canPay.getCode())) {
            return ResponseDo.newFailedDo(canPay.getInfo());
        }
        String type = "deduct".equals(deductType) ? setTypeAndSerialNo(request) : Constants.payOrderType.PAYCENTER_DEDUCTBATCH_TYPE;
        LoanOrderDO loanOrder = saveDeductLoanOrder(request, person.getId(), borrow.getId(), type, Constants.PayStyleConstants.PAY_JHH_YSB_CODE_VALUE);
        //从快速编码表查出手续费  1：代收
        String fee = codeValueMapper.getMeaningByTypeCode("payment_fee", "1");
        //修改 在第三方受理成功之前，生成手续费订单
        saveFeeOrder(loanOrder, fee);
        //发起代扣请求，请求统一支付中心
        Float finalAmount = (new BigDecimal(request.getOptAmount()).add(new BigDecimal(fee))).floatValue();
        //发起代扣
        TradeVo vo = new TradeVo(person.getId(), loanOrder.getSerialNo(), request.getPayChannel(),
                Integer.parseInt(request.getTriggerStyle()), request.getBankNum(), finalAmount,
                request.getValidateCode(),request.getMsgChannel(),request.getPayType());
        return ResponseDo.newSuccessDo(vo);
    }


    private String setTypeAndSerialNo(AgentDeductRequest request) {
        String type;
        if (PayTriggerStyleEnum.USER_TRIGGER.getCode().toString().equals(request.getTriggerStyle())) {
            type = Constants.payOrderType.PAYCENTER_DEDUCT_TYPE;
        } else {
            type = Constants.payOrderType.PAYCENTER_INITIATIVE_TYPE;
        }
        return type;
    }

    @Override
    public ResponseDo<?> refund(AgentRefundRequest refund) {
        //加入redis锁 防止重复提交
        if (!"OK".equals(jedisCluster.set(RedisConst.PAY_REFUND_KEY + refund.getPerId(), "off", "NX", "EX", 3 * 60))) {
            return new ResponseDo<>(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode(), "当前有还款在处理中，请稍后");
        }

//        if(verifyLoanOrderStatus(refund.getPerId(),Constants.payOrderType.PAYCENTER_REFUND_PAY_TYPE)){
//            return ResponseDo.newFailedDo("存在未完成的退款，请先稍等");
//        }

        //获取银行卡id
        Bank bank = bankMapper.selectByBankNumEffective(refund.getBankNum(), refund.getPerId());
        if (bank == null || !bank.getPerId().equals(refund.getPerId())) {
            return ResponseDo.newFailedDo("该银行卡不存在，请验证");
        }

        //生成订单
        LoanOrderDO loanOrder = savePayLoanOrder(refund, bank.getId(), Constants.payOrderType.PAYCENTER_REFUND_PAY_TYPE, Constants.PayStyleConstants.PAY_JHH_YSB_CODE_VALUE);
        String fee = codeValueMapper.getMeaningByTypeCode("payment_fee", "5");
        saveFeeOrder(loanOrder, fee);

        //发起付款
        TradeVo tradeVo = new TradeVo(refund.getPerId(), loanOrder.getSerialNo(), refund.getPayChannel(),
                refund.getTriggerStyle(), bank.getBankNum(), refund.getAmount().floatValue());
        ResponseDo<String> result = new ResponseDo<>();
        try {
            ResponseDo<String> responseDo = tradeLocalService.refund(tradeVo);
            log.info("支付中心退款返回结果 responseDo = {}", responseDo);
            result.setCode(responseDo.getCode());
            result.setInfo(responseDo.getInfo());
            result.setData(loanOrder.getSerialNo());
            return result;
        } catch (Exception e) {
            log.error("出错：" + e.getMessage(), e);
            result.setCode(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode());
            result.setInfo(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getMsg());
            return result;
        } finally {
            jedisCluster.del(RedisConst.PAY_REFUND_KEY + refund.getPerId());
        }
    }

    /**
     * 组装白卡代付需要数据
     * @param dto
     * @return
     */
    private BkAssetModel getPayBaiKaData(PersonInfoDto dto){
        if(null != dto && dto.getBorrowList() != null && dto.getPerson() != null){

            //查询用户详细信息
            PrivateVo privateVo = companyAService.queryUserPrivateByPhone(dto.getPerson().getPhone());
            //白卡用户信息
            PersonInfo personInfo = personInfoMapper.selectByPersonId(dto.getPerson().getId());
            if(privateVo != null && personInfo != null){
                BkAssetModel bkAssetModel = new BkAssetModel();
                bkAssetModel.setPhone(dto.getPerson().getPhone());
                bkAssetModel.setAssetNo(dto.getBorrowList().getBorrNum());
                bkAssetModel.setChannelCode("BK001");
                bkAssetModel.setName(privateVo.getName());
                bkAssetModel.setCard(privateVo.getCardNum());

                String sex = privateVo.getCardNum().substring(privateVo.getCardNum().length() - 2, privateVo.getCardNum().length() - 1);
                sex = Integer.valueOf(sex) % 2 == 0 ? "女" : "男";
                bkAssetModel.setSex(sex);
                bkAssetModel.setEducation(privateVo.getEducation());
                bkAssetModel.setMaritalStatus(getMarryStatus(privateVo.getMarry()));
                bkAssetModel.setUserType("自然人");
                bkAssetModel.setIndustry(privateVo.getProfession());
                bkAssetModel.setAccount(BigDecimal.valueOf(dto.getBorrowList().getBorrAmount()));
                bkAssetModel.setTimeLimit(1);//借款期限
                bkAssetModel.setTimeLimitType(1);//借款期限类型 1月，2年，3天
                bkAssetModel.setLoanUse(StringUtils.isEmpty(dto.getBorrowList().getLoanUse())?"资金周转":dto.getBorrowList().getLoanUse());//借款用途
                bkAssetModel.setStyle(2);//还款方式  0等额本息，1先息后本，2到期本息
                bkAssetModel.setRepaymentSource("个人收入");//还款来源
                bkAssetModel.setMonthlyIncome(5000L);//月收入
                bkAssetModel.setDebt(personInfo.getDebtInfo());//债务情况
                bkAssetModel.setHousingConditions(personInfo.getLiveInfo().indexOf("有房") > 0 ? 1 : 0);//住房条件 0无, 1有
                bkAssetModel.setDriverLicense(personInfo.getTransInfo().equals("无") ? 0 : 1);//是否购车  0无, 1有
                bkAssetModel.setCredit(1);//是否有身份证 0无, 1有
                bkAssetModel.setHousehold(0);//户口本  0无, 1有
                bkAssetModel.setCreditReport(1);//征信报告  0无, 1有
                bkAssetModel.setSerialNumber(0);//用户对公银行流水号   0无, 1有
                bkAssetModel.setWorkCertificate(0);//工作证明   0无, 1有
                bkAssetModel.setMarriageCertificate(privateVo.getMarry().equals("已婚")? 1 : 2);//婚姻证明  0无, 1有
//                bkAssetModel.setBorrowDescribe("I need money");//借款描述
                bkAssetModel.setFiled1(dto.getBorrowList().getInterestRate().multiply(new BigDecimal(100)).toString());//申请的费率
                bkAssetModel.setFiled2(dto.getBorrowList().getId().toString());//资产端借款id
                bkAssetModel.setFlilList(getBaikaFileData(privateVo, dto.getPerson()));//文件信息：json串： {“identify”:[{“name”:””,”url”:””,}],“credit”: [{“name”:””,”url”:””,}]}

                return bkAssetModel;
            }
        }

        return null;
    }

    private String getBaikaFileData(PrivateVo privateVo, Person person){
        //征信信息
        String juxinliUrl = riskReportApiUrl + polyXinliUrl + "?phone=" + person.getPhone() + "&idcard=" + person.getCardNum();
        Map identify = new HashMap();
        identify.put("identifyPositive",privateVo.getImageZ());
        identify.put("identifyOpposite",privateVo.getImageF());
        identify.put("credit",juxinliUrl);
        return JSONObject.toJSONString(identify);
    }

    public static void main(String[] arge){
        String card =  "132465798";
        String num = card.substring(card.length() - 2, card.length() - 1);

    }

    private int getMarryStatus(String marry){
        if(Detect.notEmpty(marry)){
            switch(marry){
                case "已婚":
                    return 0;
                case "未婚":
                    return 1;
                case "离异":
                    return 2;
                case "丧偶":
                    return 3;
            }
        }
        return 99;
    }

}
