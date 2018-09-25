package com.jhh.dc.baika.service.channel;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.mapper.app.BankMapper;
import com.jhh.dc.baika.mapper.app.PersonMapper;
import com.jhh.dc.baika.mapper.gen.LoanOrderDOMapper;
import com.jhh.dc.baika.mapper.loan.PayChannelAdapterMapper;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.channel.TradeBatchStateService;
import com.jhh.dc.baika.api.channel.TradePayService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.LoanOrderVO;
import com.jhh.dc.baika.api.entity.capital.TradeBatchVo;
import com.jhh.dc.baika.api.entity.capital.TradeVo;
import com.jhh.dc.baika.common.enums.AgentDeductResponseEnum;
import com.jhh.dc.baika.common.enums.AgentpayResultEnum;
import com.jhh.dc.baika.common.enums.PayChannelEnum;
import com.jhh.dc.baika.common.enums.PayTriggerStyleEnum;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.entity.app.Bank;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.mapper.gen.domain.LoanOrderDO;
import com.jhh.dc.baika.service.capital.BasePayServiceImpl;
import com.jhh.pay.driver.pojo.*;
import com.jhh.pay.driver.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.math.BigDecimal;
import java.util.*;

/**
 * 支付中心代付代扣操作
 */
@Slf4j
@Service
public class TradePayCenterServiceImpl extends BasePayServiceImpl implements TradePayService, TradeBatchStateService {

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private PayChannelAdapterMapper payChannelAdapterMapper;


    @Value("${isTest}")
    private String isTest;

    @Value("${batchDeduct.notifyUrl}")
    private String notifyUrl;
    @Autowired
    private TradeService tradeService;

    @Override
    public ResponseDo<String> postPayment(TradeVo vo) {
        Bank bank = bankMapper.selectByBankNumAndStatus(vo.getBankNum());
        Person p = personMapper.selectByPrimaryKey(vo.getPerId());
        Map<String, Object> ext = new HashMap<>();
        ext.put("callbackUrl", "www.baidu.com");
        Map<String, Object> channelMap = new HashMap<>();
        if (!StringUtils.isEmpty(vo.getPayChannel())) {
            channelMap.put(vo.getPayChannel(), new JSONObject());
        }
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankCard(bank.getBankNum());
        bankInfo.setBankNo(bank.getBankCode());
        bankInfo.setPersonalName(p.getName());
        bankInfo.setBankMobile(bank.getPhone());
        PayRequest request = new PayRequest();
        request.setOrderNo(vo.getSerialNo());
        request.setOrderTime(System.currentTimeMillis() / 1000);
        request.setMoney(vo.getAmount());
        request.setAppId(Constants.PayStyleConstants.DC_PAY_APPID);
        request.setAsync(false);
        request.setActiveQuery(true);
        request.setChannels(channelMap);
        request.setExtension(ext);
        request.setNotifyUrl(" ");//假的回调，支付中心也不会来调
        request.setSign("123456");
        request.setRespTimeout(30);
        request.setBankInfo(bankInfo);
        try {
            PayResponse response = tradeService.pay(request);
            log.info("\n###########支付中心返回\n{}", response);
            if (response == null) {
                return ResponseDo.newFailedDo("支付渠道出现异常，请稍候");
            }
            if ("on".equals(isTest)) {
                response.setState("PROGRESSING");
                response.setMsg("测试默认通过");
            }
            return verifyChannelPayResponse(vo.getSerialNo(), response);
        } catch (Exception e) {
            log.error("支付中心代付异常", e);
            return null;
        }
    }

    @Override
    public ResponseDo<String> postDeduct(TradeVo vo) {
        Bank bank = bankMapper.selectByBankNumAndStatus(vo.getBankNum());
        Person p = personMapper.selectByPrimaryKey(vo.getPerId());
        ResponseDo<String> result = new ResponseDo<>();
        //封装请求参数
        PayRequest payRequest = new PayRequest();
        if (PayTriggerStyleEnum.USER_TRIGGER.getCode().equals(vo.getTriggerStyle())) {
            payRequest.setAppId(Constants.PayStyleConstants.DC_REPAY_SWITCH);
        } else {
            payRequest.setAppId(Constants.PayStyleConstants.DC_DEDUCT_APPID);
        }
        payRequest.setActiveQuery(true);
        payRequest.setAsync(false);
        payRequest.setOrderNo(vo.getSerialNo());
        payRequest.setMoney(vo.getAmount());
        payRequest.setOrderTime(System.currentTimeMillis() / 1000);

        if(StringUtils.isNotEmpty(vo.getPayChannel())){
            //传入支付渠道
            Map<String, Object> channelMap = new HashMap<>();
            channelMap.put(vo.getPayChannel(),new HashMap<>());

            payRequest.setChannels(channelMap);
        }
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankCard(bank == null ? null : bank.getBankNum());//银行卡号
        bankInfo.setPersonalName(p.getName());
        bankInfo.setBankNo(bank.getBankCode());
        bankInfo.setBankMobile(bank == null ? null : bank.getPhone());
        payRequest.setBankInfo(bankInfo);

        Map<String, Object> ext = new HashMap<>();
        ext.put("user_id", vo.getPerId());
        ext.put("idCardNo", p.getCardNum());
        ext.put("type", vo.getPayType());
        payRequest.setExtension(ext);
        payRequest.setSign("12345");
        payRequest.setNotifyUrl("www.baidu.com");
        log.info("请求参数:" + payRequest);
        try {
            PayResponse payResponse = tradeService.deduct(payRequest);
            log.info("请求金互行统一支付中心返回:{}", payResponse);
            if (payResponse == null) {
                return ResponseDo.newFailedDo("支付出现异常，请稍候再试");
            }
            ResponseDo<PayResponse> processResult = processResult(payResponse);
            verifyChannelDeductResponse(vo.getSerialNo(), processResult);
            result.setCode(processResult.getCode());
            result.setInfo(processResult.getInfo());
            if (PayTriggerStyleEnum.USER_TRIGGER.getCode().equals(vo.getTriggerStyle())) {
                if (payResponse.getExtension() != null && payResponse.getExtension().size() > 0) {
                    result.setData((String) payResponse.getExtension().get("zfbUrl"));
                }
            } else {
                result.setData(vo.getSerialNo());
            }
            return result;
        } catch (Exception e) {
            log.error("支付出现异常，请稍候再试", e);
            return ResponseDo.newFailedDo("支付出现异常，请稍候再试");
        }
    }

    @Override
    public ResponseDo<?> state(String serialNo) {
        log.info("[查询开始] -->走支付中心渠道 查询 serNo" + serialNo);
        ResponseDo<PayResponse> responseDo = new ResponseDo<>(204, "该笔订单已经处理完成，请返回查看");
        LoanOrderDO orderDO = loanOrderDOMapper.selectBySerNo(serialNo);
        if ("p".equals(orderDO.getRlState())) {
            //查询路由选择
            LoanOrderVO vo = new LoanOrderVO();
            try {
                BeanUtils.copyProperties(orderDO, vo);
                responseDo = this.doState(vo);
                //如何订单sid = null 加入 sid
                if (StringUtils.isEmpty(orderDO.getSid()) || StringUtils.isEmpty(orderDO.getChannel())) {
                    orderDO.setSid(responseDo.getData().getSid());
                    String channel = payChannelAdapterMapper.getChannelBypayCenterAndType(responseDo.getData().getChannelKey(), orderDO.getType());
                    if (StringUtils.isNotEmpty(channel)){
                        orderDO.setChannel(channel);
                    }else {
                        orderDO.setChannel(responseDo.getData().getChannelKey());
                    }
                    loanOrderDOMapper.updateByPrimaryKeySelective(orderDO);
                }
                //查询后续处理操作
                afterState(responseDo);
                return responseDo;
            } catch (Exception e) {
                log.info("出错：" + e.getMessage(), e);
                return new ResponseDo<>(204, "处理中,请稍候");
            }
        }
        return responseDo;
    }

    @Override
    public ResponseDo<?> batchDeduct(TradeBatchVo vo) {
        log.info("[批量开始] -->走支付中心渠道 批量 TradeBatchVo" + vo);
        BatchTradeRequest request = new BatchTradeRequest();
        request.setAppId(Constants.PayStyleConstants.DC_BATCH_APPID);
        List<Order> orders = saveBatchOrder(vo);
        request.setDeductList(orders);
        //获取订单总金额
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < vo.getDeduct().size(); i++) {
            if (vo.getDeduct().get(i).getAmount() != null) {
                sum = sum.add(new BigDecimal(String.valueOf(vo.getDeduct().get(i).getAmount())));
            }
        }
        request.setDeductAmount(String.valueOf(sum));
        request.setDeductSize(String.valueOf(vo.getDeductSize()));
        request.setPurpose("批量代收");
        request.setOptPerson(vo.getOptPerson());
        request.setTs(System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("notifyUrl", notifyUrl);
        request.setExtension(map);
        try {
            TradeBatchResponse tradeBatchResponse = tradeService.batchTrade(request);
            log.info("批量代扣返回结果 batchTradeResponse = " + tradeBatchResponse);
            if (tradeBatchResponse == null) {
                throw new Exception("支付中心批量代扣发生异常");
            }
            ResponseDo<?> responseDo = veifyState(tradeBatchResponse.getState(), tradeBatchResponse.getMsg());
            veifyBatchDeduct(vo.getDeduct(), responseDo, tradeBatchResponse);
            return responseDo;
        } catch (Exception e) {
            log.error("支付中心批量代扣发生异常", e);
            return ResponseDo.newFailedDo("支付中心批量代扣发生异常");
        }

    }

    @Override
    public ResponseDo<String> refund(TradeVo tradeVo) throws Exception {
        log.info("开始退款，请求参数 tradeVo = {}", tradeVo);
        Bank bank = bankMapper.selectByBankNumEffective(tradeVo.getBankNum(), tradeVo.getPerId());
        Person p = personMapper.selectByPrimaryKey(tradeVo.getPerId());
        Map<String, Object> ext = new HashMap<>();
        ext.put("callbackUrl", "www.baidu.com");
        Map<String, Object> channelMap = new HashMap<>();
        if (!StringUtils.isEmpty(tradeVo.getPayChannel())) {
            channelMap.put(tradeVo.getPayChannel(), new JSONObject());
        }
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankCard(bank.getBankNum());
        bankInfo.setPersonalName(p.getName());
        bankInfo.setBankMobile(bank.getPhone());
        PayRequest request = new PayRequest();
        request.setOrderNo(tradeVo.getSerialNo());
        request.setOrderTime(System.currentTimeMillis() / 1000);
        request.setMoney(tradeVo.getAmount());
        request.setAppId(Constants.PayStyleConstants.DC_PAY_APPID);
        request.setAsync(false);
        request.setActiveQuery(true);
        request.setChannels(channelMap);
        request.setExtension(ext);
        request.setNotifyUrl(" ");//假的回调，支付中心也不会来调
        request.setSign("123456");
        request.setRespTimeout(30);
        request.setBankInfo(bankInfo);
        try {
            PayResponse response = tradeService.pay(request);
            log.info("支付中心返回的结果 PayResponse = {}", response);
            if (response == null) {
                return ResponseDo.newFailedDo("支付渠道出现异常，请稍候");
            }
            if ("on".equals(isTest)) {
                response.setState("PROGRESSING");
                response.setMsg("测试默认通过");
            }
            return verifyChannelPayResponse(tradeVo.getSerialNo(), response);
        } catch (Exception e) {
            log.error("支付中心代付异常", e);
            return ResponseDo.newFailedDo("支付中心代付【退款】异常");
        }
    }

    @Override
    public ResponseDo<PayResponse> batchState(List<String> serialNo) {
        BatchQueryRequest batchQueryRequest = new BatchQueryRequest();
        batchQueryRequest.setAppId(Constants.PayStyleConstants.DC_BATCH_APPID);
        batchQueryRequest.setTs(System.currentTimeMillis() / 1000);
        batchQueryRequest.setSign("123456");
        batchQueryRequest.setOrderNos(serialNo);
        try {
            PayResponse response = tradeService.stateBatch(batchQueryRequest);
            log.info("支付中心批量查询返回的结果 PayResponse = {}", response);
            if (response == null) {
                return new ResponseDo<>(204, "支付中心出现错误");
            }
            return ResponseDo.newSuccessDo(response);
        } catch (Exception e) {
            log.error("支付中心抛出异常", e);
            return new ResponseDo<>(204, "支付中心出现错误");
        }

    }

    private void veifyBatchDeduct(List<TradeVo> deduct, ResponseDo<?> responseDo, TradeBatchResponse tradeBatchResponse) {

        deduct.forEach(v -> {
            LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(v.getSerialNo());
            loanOrderDO.setPayChannel(PayChannelEnum.JHH_YSB.getCode());
            String channel = payChannelAdapterMapper.getChannelBypayCenterAndType(tradeBatchResponse.getChannelKey(), loanOrderDO.getType());
            if (StringUtils.isNotEmpty(channel)){
                loanOrderDO.setChannel(channel);
            }else {
                loanOrderDO.setChannel(tradeBatchResponse.getChannelKey());
            }
            loanOrderDO.setSid(tradeBatchResponse.getSid());
            loanOrderDOMapper.updateByPrimaryKeySelective(loanOrderDO);
            if (200 != responseDo.getCode()) {
                //第三方受理失败
                updateOrderForFail(loanOrderDO, responseDo.getInfo());
            }
        });
    }


    /**
     * 批量代扣参数设置
     *
     * @param vo
     * @return
     */
    private List<Order> saveBatchOrder(TradeBatchVo vo) {
        List<Order> orders = new ArrayList<>();
        vo.getDeduct().forEach(v -> {
            Bank bank = bankMapper.selectByBankNumAndStatus(v.getBankNum());
            Person p = personMapper.selectByPrimaryKey(v.getPerId());
            Order order = new Order();
            order.setOrderNo(v.getSerialNo());
            order.setMoney(new BigDecimal(v.getAmount().toString()));
            order.setOrderTime(new Date());
            order.setAppId(Constants.PayStyleConstants.DC_BATCH_APPID);
            //传入支付渠道
            Map<String, Object> channelMap = new HashMap<>();
            if (StringUtils.isNotEmpty(vo.getPayChannel())) {
                channelMap.put(vo.getPayChannel(), new JSONObject());
            }
            order.setChannels(channelMap);
            BankInfo bankInfo = new BankInfo();
            bankInfo.setBankCard(bank.getBankNum());//银行卡号
            bankInfo.setPersonalName(p.getName());
            bankInfo.setBankMobile(bank.getPhone());
            bankInfo.setBankName(bank.getBankName());
            bankInfo.setBankNo(bank.getBankCode());
            order.setBankInfo(bankInfo);

            Map<String, Object> ext = new HashMap<>();
            ext.put("idCardNo", p.getCardNum());
            order.setExtension(ext);
            orders.add(order);
        });
        return orders;
    }

    /**
     * 验证支付中心返回参数
     *
     * @param serialNo
     * @param response
     * @return
     */
    private ResponseDo<String> verifyChannelPayResponse(String serialNo, PayResponse response) throws Exception {
        //更改订单信息
        LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(serialNo);
        loanOrderDO.setSid(response.getSid());
        loanOrderDO.setPayChannel(PayChannelEnum.JHH_YSB.getCode());
        //新增适配
        String channel = payChannelAdapterMapper.getChannelBypayCenterAndType(response.getChannelKey(), loanOrderDO.getType());
        if (StringUtils.isNotEmpty(channel)){
            loanOrderDO.setChannel(channel);
        }else {
            loanOrderDO.setChannel(response.getChannelKey());
        }
        int opResult = loanOrderDOMapper.updateByPrimaryKeySelective(loanOrderDO);
        //支付中心内部错误
        if (Constants.PayStyleConstants.JHH_PAY_STATE_ERROR.equals(response.getState())
                || Constants.PayStyleConstants.JHH_PAY_STATE_FAIL.equals(response.getState())) {
            log.error("支付中心订单处理失败 失败原因为----" + response.getMsg());
            handleFail(loanOrderDO, response.getMsg());
            return ResponseDo.newFailedDo(response.getMsg());
        }
        //第三方支付已经接受处理了
        if (Constants.PayStyleConstants.JHH_PAY_STATE_SUCCESS.equals(response.getState())
                || Constants.PayStyleConstants.JHH_PAY_STATE_PROGRESSING.equals(response.getState())) {
            if (opResult == 1) {
                log.info("\n [代付] 调用金互行支付中心银生宝 返回正在处理中,完美");
                return ResponseDo.newSuccessDo();
            } else {
                throw new RuntimeException("记录条数出错");
            }
        } else {
            log.error("\n[代付] 支付中心返回 未知的state");
            return ResponseDo.newFailedDo(AgentpayResultEnum.JHH_PAY_ERROR.getDesc());
        }
    }

    /**
     * 代扣结果返回
     *
     * @param response
     * @return
     */
    private ResponseDo<PayResponse> processResult(PayResponse response) {
        ResponseDo<PayResponse> result = ResponseDo.newSuccessDo();
        if (Constants.PayStyleConstants.JHH_PAY_STATE_PROGRESSING.equals(response.getState())
                || Constants.PayStyleConstants.JHH_PAY_STATE_SUCCESS.equals(response.getState())) {
            result.setCode(AgentDeductResponseEnum.SUCCESS_CODE.getCode());
        } else if (Constants.PayStyleConstants.JHH_PAY_STATE_ERROR.equals(response.getState())) {
            result.setCode(AgentDeductResponseEnum.BUSINESS_SYSTEM_ERROR.getCode());
        } else if (Constants.PayStyleConstants.JHH_PAY_STATE_FAIL.equals(response.getState())) {
            result.setCode(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode());
        } else {
            result.setCode(AgentDeductResponseEnum.SUCCESS_CODE.getCode());
        }
        result.setInfo(response.getMsg());
        result.setData(response);
        return result;
    }

    /**
     * 验证批量第三方返回状态
     *
     * @param state 返回码
     * @param msg   信息
     * @return
     */
    private ResponseDo<?> veifyState(String state, String msg) {
        ResponseDo<?> result = new ResponseDo<>();
        if (Constants.PayStyleConstants.JHH_PAY_STATE_PROGRESSING.equals(state)
                || Constants.PayStyleConstants.JHH_PAY_STATE_SUCCESS.equals(state)) {
            result.setCode(AgentDeductResponseEnum.SUCCESS_CODE.getCode());
        } else if (Constants.PayStyleConstants.JHH_PAY_STATE_ERROR.equals(state)) {
            result.setCode(AgentDeductResponseEnum.BUSINESS_SYSTEM_ERROR.getCode());
        } else if (Constants.PayStyleConstants.JHH_PAY_STATE_FAIL.equals(state)) {
            result.setCode(AgentDeductResponseEnum.BUSINESS_INNER_PARAM_ERROR.getCode());
        } else {
            result.setCode(AgentDeductResponseEnum.SUCCESS_CODE.getCode());
        }
        result.setInfo(msg);
        return result;
    }

    /**
     * 查询接口
     *
     * @param vo 参数
     * @return
     * @throws Exception
     */
    private ResponseDo<PayResponse> doState(LoanOrderVO vo) throws Exception {
        ResponseDo<PayResponse> responseDo = new ResponseDo<>();
        ActiveQueryRequest request = new ActiveQueryRequest();
        request.setSid(vo.getSid());
        request.setOrderNo(vo.getSerialNo());
        request.setTs(System.currentTimeMillis() / 1000);
        request.setSign("123");
        PayResponse state = tradeService.state(request);
        if (state == null) {
            throw new Exception("查询tradeService.state出错,返回空");
        }
        log.info("---------支付中心查询结果 state = {}", state);
        responseDo.setInfo(state.getMsg());
        responseDo.setData(state);
        responseDo.setCode(200);
        return responseDo;
    }

    /**
     * 查询后续处理过程：清结算
     * 注：订单状态：处理中时，不做任何处理，也包括不做清结算处理
     */
    @SuppressWarnings("Duplicates")
    private void afterState(ResponseDo<PayResponse> result) throws Exception {
        //处理中状态
        if ("SUCCESS".equals(result.getData().getState())) {
            result.setCode(Constants.DeductQueryResponseConstants.SUCCESS_CODE);
            result.setInfo("交易成功");
        } else if ("PROGRESSING".equals(result.getData().getState())) {
            result.setCode(Constants.DeductQueryResponseConstants.PROGRESSING);
            result.setInfo(result.getData().getMsg());
        } else if ("ERROR".equals(result.getData().getState()) || "FAIL".equals(result.getData().getState())) {
            result.setCode(Constants.DeductQueryResponseConstants.SUCCESS_ORDER_SETTLE_FAIL);
            result.setInfo(result.getData().getMsg());
        } else {
            result.setCode(Constants.DeductQueryResponseConstants.SYSTEM_ERROR_CODE);
            result.setInfo("支付中心状态出错");
        }
    }

    private void verifyChannelDeductResponse(String loanOrder, ResponseDo result) throws Exception {
        LoanOrderDO loanOrderDO = loanOrderDOMapper.selectBySerNo(loanOrder);
        PayResponse response = (PayResponse) result.getData();
        loanOrderDO.setPayChannel(PayChannelEnum.JHH_YSB.getCode());
        loanOrderDO.setSid(response.getSid());
        //适配渠道
        String channel = payChannelAdapterMapper.getChannelBypayCenterAndType(response.getChannelKey(), loanOrderDO.getType());
        if (StringUtils.isNotEmpty(channel)){
            loanOrderDO.setChannel(channel);
        }else {
            loanOrderDO.setChannel(response.getChannelKey());
        }
        loanOrderDOMapper.updateByPrimaryKeySelective(loanOrderDO);
        if (200 != result.getCode()) {
            //第三方受理失败
            handleFail(loanOrderDO, result.getInfo());
        }
    }
}
