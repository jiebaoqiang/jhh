package com.jhh.dc.baika.service.bankdeposit;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositWebService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.common.enums.QianQiDepositEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.HttpUtils;
import com.jhh.dc.baika.common.util.HttpsUtil;
import com.jhh.dc.baika.common.util.MapUtils;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.app_vo.RabbitMessage;
import com.jhh.dc.baika.entity.bankdeposit.*;
import com.jhh.dc.baika.entity.enums.BorrowStatusEnum;
import com.jhh.dc.baika.entity.manager.Order;
import com.jhh.dc.baika.mapper.app.BorrowListMapper;
import com.jhh.dc.baika.mapper.contract.ContractMapper;
import com.jhh.dc.baika.mapper.manager.OrderMapper;
import com.jhh.dc.baika.service.capital.BasePayServiceImpl;
import com.jhh.dc.baika.service.capital.BaseServiceImpl;
import com.jinhuhang.settlement.dto.RepaymentFromYoutu;
import com.jinhuhang.settlement.dto.SettlementResult;
import com.jinhuhang.settlement.service.SettlementAPI;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class QianQiDepositWebImpl extends BasePayServiceImpl implements QianQiDepositWebService {


    @Value("${qianQi_Bank_Deposit_Request_Url}")
    private String bankDepositRequestUrl;

    @Value("${youtubaika_request_url}")
    private String bankDepositQueryUrl;

    @Value("${youtu_query_withfee_url}")
    private String queryFeeUrl;


    @Value("${a_company_sync_url}")
    private String syncACompanyBorrUrl;

    @Value("${a_company_sync_refund_url}")
    private String syncACompayRefundUrl;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private SettlementAPI settlementAPI;


    @Autowired
    private OrderMapper orderMapper;

    /**
     * 判断用户是否允许注册true允许  false不允许
     * @param registerPhone
     * @return
     */
    @Override
    public boolean checkIsAgreeRegister(String registerPhone) {
        log.info("前旗银行存管《判断用户是否允许注册》接口【checkIsAgreeRegister】接口参数registerPhone:{}",registerPhone);
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;

        CheckRegisterPhoneRequest checkRegisterPhoneRequest=new CheckRegisterPhoneRequest();
        checkRegisterPhoneRequest.setPhone(registerPhone);
        log.info("前旗银行存管《判断用户是否允许注册》接口【checkIsAgreeRegister】接口请求数据url:{},data:{}",queryFeeUrl+"cms/baika/phone/check", checkRegisterPhoneRequest);
        result= HttpsUtil.sendPost(queryFeeUrl+"cms/baika/phone/check", MapUtils.ConvertObjToMap(checkRegisterPhoneRequest));
        log.info("前旗银行存管《判断用户是否允许注册》接口【checkIsAgreeRegister】悠兔接口响应数据result:{}",result);

        if(StringUtils.isEmpty(result)){
            return false;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
            return false;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            return false;
        }
        return true;
    }

    /**
     * 前旗银行存管开户接口
     * @param registerPhone
     * @return
     */
    @Override
    public ResponseDo<BankDepositCommon> openAccount(String registerPhone, String webResponseUrl) {
        log.info("前旗银行存管《开户》接口【openAccount】请求数据registerPhone:{}",registerPhone);
        ResponseDo<BankDepositCommon>  responseDo=null;
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;
        OpenAccountRequest openAccount=new OpenAccountRequest();
        openAccount.setResponsePath(webResponseUrl);
        openAccount.setCustType(QianQiDepositEnum.CUST_TYPE_LOAN.getCode());
        openAccount.setRegisterPhone(registerPhone);
        openAccount.setTradeCode(QianQiDepositEnum.TRADE_CODE_OPEN_ACCOUNT.getCode());
        log.info("前旗银行存管《开户》接口【openAccount】接口请求数据url:{},data:{}",bankDepositRequestUrl, openAccount);
         result= HttpsUtil.sendPost(bankDepositRequestUrl, MapUtils.ConvertObjToMap(openAccount));
        log.info("前旗银行存管《开户》接口【openAccount】悠兔接口响应数据result:{}",result);
        if(StringUtils.isEmpty(result)){
            //responseDo=ResponseDo.newFailedDo("前旗银行《开户》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《开户》接口异常！");
            return responseDo;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
            //responseDo=ResponseDo.newFailedDo("前旗银行《开户》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《开户》接口异常！");
            return responseDo;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            if(StringUtil.isEmpty(retMsg))
                responseDo=ResponseDo.newFailedDo("前旗银行《开户》接口异常！");
            else
                responseDo=ResponseDo.newFailedDo(retMsg);

            return responseDo;
        }
        BankDepositCommon bankDepositCommon=JSONObject.parseObject(retData,BankDepositCommon.class);
        responseDo=ResponseDo.newSuccessDo();
        responseDo.setData(bankDepositCommon);
        return responseDo;

    }

    /**
     * 前旗存管授权接口
     * @param custAuthRequest
     * @return
     */
    @Override
    public ResponseDo<BankDepositCommon> custAuth(CustAuthRequest custAuthRequest) {
        log.info("前旗银行存管《授权》接口【custAuth】custAuthRequest:{}",custAuthRequest);
        ResponseDo<BankDepositCommon> responseDo=null;
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;

        custAuthRequest.setTradeCode(QianQiDepositEnum.TRADE_CODE_CUST_AUTH.getCode());
        custAuthRequest.setAmount(QianQiDepositEnum.AUTH_AMOUNT.getCode());
        custAuthRequest.setExpiryTime(QianQiDepositEnum.AUTH_EXPIRY_TIME.getCode());
        log.info("前旗银行存管《授权》接口【custAuth】接口请求数据url:{},data:{}",bankDepositRequestUrl, custAuthRequest);
        result= HttpsUtil.sendPost(bankDepositRequestUrl, MapUtils.ConvertObjToMap(custAuthRequest));
        log.info("前旗银行存管《授权》接口【custAuth】悠兔接口响应数据result:{}",result);
        if(StringUtils.isEmpty(result)){
            //responseDo=ResponseDo.newFailedDo("前旗银行《授权》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《授权》接口异常！");
            return responseDo;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
            //responseDo=ResponseDo.newFailedDo("前旗银行《授权》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《授权》接口异常！");
            return responseDo;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            if(StringUtil.isEmpty(retMsg))
                responseDo=ResponseDo.newFailedDo("前旗银行《授权》接口异常！");
            else
                responseDo=ResponseDo.newFailedDo(retMsg);

            return responseDo;
        }

        BankDepositCommon bankDepositCommon=JSONObject.parseObject(retData,BankDepositCommon.class);
        responseDo=ResponseDo.newSuccessDo();
        responseDo.setData(bankDepositCommon);
        return responseDo;
    }
    /**
     * 前旗存管修改密码接口
     * @param editPayPwdRequest
     * @return
     */
    @Override
    public ResponseDo<BankDepositCommon> editPayPwd(EditPayPwdRequest editPayPwdRequest) {
        log.info("前旗银行存管《修改交易密码》接口【editPayPwd】editPayPwdRequest:{}",editPayPwdRequest);
        ResponseDo<BankDepositCommon> responseDo=null;
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;

        editPayPwdRequest.setTradeCode(QianQiDepositEnum.TRADE_CODE_EDIT_PWD.getCode());
        editPayPwdRequest.setPswordCode(QianQiDepositEnum.PWD_TYPE_PAY.getCode());
        log.info("前旗银行存管《修改交易密码》接口【editPayPwd】接口请求数据url:{},data:{}",bankDepositRequestUrl, editPayPwdRequest);
        result= HttpsUtil.sendPost(bankDepositRequestUrl, MapUtils.ConvertObjToMap(editPayPwdRequest));
        log.info("前旗银行存管《修改交易密码》接口【editPayPwd】悠兔接口响应数据result:{}",result);
        if(StringUtils.isEmpty(result)){
            //responseDo=ResponseDo.newFailedDo("前旗银行《修改交易密码》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《修改交易密码》接口异常！");
            return responseDo;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
            //responseDo=ResponseDo.newFailedDo("前旗银行《修改交易密码》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《修改交易密码》接口异常！");
            return responseDo;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            if(StringUtil.isEmpty(retMsg))
                responseDo=ResponseDo.newFailedDo("前旗银行《修改交易密码》接口异常！");
            else
                responseDo=ResponseDo.newFailedDo(retMsg);

            return responseDo;
        }

        BankDepositCommon bankDepositCommon=JSONObject.parseObject(retData,BankDepositCommon.class);
        responseDo=ResponseDo.newSuccessDo();
        responseDo.setData(bankDepositCommon);
        return responseDo;
    }
    /**
     * 前旗银行重置密码接口
     * @param resetPayPwdRequest
     * @return
     */
    @Override
    public ResponseDo<BankDepositCommon> resetPayPwd(ResetPayPwdRequest resetPayPwdRequest) {
        log.info("前旗银行存管《重置交易密码》接口【resetPayPwd】resetPayPwdRequest:{}",resetPayPwdRequest);
        ResponseDo<BankDepositCommon> responseDo=null;
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;

        resetPayPwdRequest.setTradeCode(QianQiDepositEnum.TRADE_CODE_RESET_PWD.getCode());
        log.info("前旗银行存管《重置交易密码》接口【resetPayPwd】接口请求数据url:{},data:{}",bankDepositRequestUrl, resetPayPwdRequest);
        result= HttpsUtil.sendPost(bankDepositRequestUrl, MapUtils.ConvertObjToMap(resetPayPwdRequest));
        log.info("前旗银行存管《重置交易密码》接口【resetPayPwd】悠兔接口响应数据result:{}",result);
        if(StringUtils.isEmpty(result)){
            //responseDo=ResponseDo.newFailedDo("前旗银行《重置交易密码》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《重置交易密码》接口异常！");
            return responseDo;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
            //responseDo=ResponseDo.newFailedDo("前旗银行《重置交易密码》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《重置交易密码》接口异常！");
            return responseDo;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            if(StringUtil.isEmpty(retMsg))
                responseDo=ResponseDo.newFailedDo("前旗银行《重置交易密码》接口异常！");
            else
                responseDo=ResponseDo.newFailedDo(retMsg);

            return responseDo;
        }

        BankDepositCommon bankDepositCommon=JSONObject.parseObject(retData,BankDepositCommon.class);
        responseDo=ResponseDo.newSuccessDo();
        responseDo.setData(bankDepositCommon);
        return responseDo;
    }

    /**
     * 前旗存管充值接口
     * @return
     */
    @Override
    public ResponseDo<BankDepositCommon> reCharge(RechargeRequest rechargeRequest) {
        log.info("前旗银行存管《充值》接口【reCharge】rechargeRequest:{}",rechargeRequest);
        ResponseDo<BankDepositCommon> responseDo=null;
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;

        rechargeRequest.setTradeCode(QianQiDepositEnum.TRADE_CODE_RE_CHARGE.getCode());
        log.info("前旗银行存管《充值》接口【reCharge】接口请求数据url:{},data:{}",bankDepositRequestUrl, rechargeRequest);
        result= HttpsUtil.sendPost(bankDepositRequestUrl, MapUtils.ConvertObjToMap(rechargeRequest));
        log.info("前旗银行存管《充值》接口【reCharge】悠兔接口响应数据result:{}",result);
        if(StringUtils.isEmpty(result)){
            //responseDo=ResponseDo.newFailedDo("前旗银行《充值》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《充值》接口异常！");
            return responseDo;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
            //responseDo=ResponseDo.newFailedDo("前旗银行《充值》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《充值》接口异常！");
            return responseDo;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            if(StringUtil.isEmpty(retMsg))
                responseDo=ResponseDo.newFailedDo("前旗银行《充值》接口异常！");
            else
                responseDo=ResponseDo.newFailedDo(retMsg);

            return responseDo;
        }

        BankDepositCommon bankDepositCommon=JSONObject.parseObject(retData,BankDepositCommon.class);
        responseDo=ResponseDo.newSuccessDo();
        responseDo.setData(bankDepositCommon);
        return responseDo;

    }

    /**
     * 前旗存管提现接口
     * @param withdrawRequest
     * @return
     */
    @Override
    public ResponseDo<BankDepositCommon> withDraw(WithdrawRequest withdrawRequest) {
        log.info("前旗银行存管《提现》接口【withDraw】rechargeRequest:{}",withdrawRequest);
        ResponseDo<BankDepositCommon> responseDo=null;
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;

        withdrawRequest.setTradeCode(QianQiDepositEnum.TRADE_CODE_WITH_DRAW.getCode());
        log.info("前旗银行存管《提现》接口【withDraw】接口请求数据url:{},data:{}",bankDepositRequestUrl, withdrawRequest);
        result= HttpsUtil.sendPost(bankDepositRequestUrl, MapUtils.ConvertObjToMap(withdrawRequest));
        log.info("前旗银行存管《提现》接口【withDraw】悠兔接口响应数据result:{}",result);
        if(StringUtils.isEmpty(result)){
            //responseDo=ResponseDo.newFailedDo("前旗银行《提现》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《提现》接口异常！");
            return responseDo;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
           // responseDo=ResponseDo.newFailedDo("前旗银行《提现》接口响应数据为空！");
            responseDo=ResponseDo.newFailedDo("前旗银行《提现》接口异常！");
            return responseDo;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            if(StringUtil.isEmpty(retMsg))
                responseDo=ResponseDo.newFailedDo("前旗银行《提现》接口异常！");
            else
                responseDo=ResponseDo.newFailedDo(retMsg);

            return responseDo;
        }

        BankDepositCommon bankDepositCommon=JSONObject.parseObject(retData,BankDepositCommon.class);
        responseDo=ResponseDo.newSuccessDo();
        responseDo.setData(bankDepositCommon);
        return responseDo;

    }

    /**
     * 前旗存管查询账户余额接口
     */
    @Override
    public String queryUserAccountAmount(String registerPhone) {
        log.info("前旗银行存管《查询账户余额》接口【queryUserAccountAmount】registerPhone:{}",registerPhone);
        ResponseDo<BankDepositCommon> responseDo=null;
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;
        String accountAmount=null;

        AccountAmountRequest accountAmountRequest=new AccountAmountRequest();
        accountAmountRequest.setTradeCode(QianQiDepositEnum.TRADE_CODE_ACCOUNT_AMOUNT.getCode());
        accountAmountRequest.setRegisterPhone(registerPhone);

        log.info("前旗银行存管《查询账户余额》接口【queryUserAccountAmount】接口请求数据url:{},data:{}",bankDepositQueryUrl+"interface/query", accountAmountRequest);
        result= HttpsUtil.sendPost(bankDepositQueryUrl+"interface/query", MapUtils.ConvertObjToMap(accountAmountRequest));
        log.info("前旗银行存管《查询账户余额》接口【queryUserAccountAmount】悠兔接口响应数据result:{}",result);

        if(StringUtils.isEmpty(result)){
            return null;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
           return null;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            return null;
        }

        accountAmount=getJsonValueByName(retData,QianQiDepositEnum.YOUTU_RESPONSE_USER_MONEY.getCode());

        return accountAmount;

    }



    /**
     * 前旗查询充值或提现手续费接口
     * @return
     */
    @Override
    public String queryWithdrawCharge() {
        log.info("前旗银行存管《提现手续费》接口【queryWithdrawCharge】接口开始");
        String result=null;
        JSONObject resultToJson=null;
        String retCode=null;
        String retMsg=null;
        String retData=null;
        String accountAmount=null;

        QueryWithdrawRequest queryWithdrawRequest=new QueryWithdrawRequest();
        queryWithdrawRequest.setIdentityType(QianQiDepositEnum.WITH_DRAW_ID_TYPE.getCode());
        queryWithdrawRequest.setFeeCode(QianQiDepositEnum.WITH_DRAW_TYPE.getCode());
        log.info("前旗银行存管《提现手续费》接口【queryWithdrawCharge】接口请求数据url:{},data:{}",queryFeeUrl+"cms/baika/poundage", queryWithdrawRequest);
        result= HttpsUtil.sendPost(queryFeeUrl+"cms/baika/poundage", MapUtils.ConvertObjToMap(queryWithdrawRequest));
        log.info("前旗银行存管《提现手续费》接口【queryWithdrawCharge】悠兔接口响应数据result:{}",result);

        if(StringUtils.isEmpty(result)){
            return null;
        }
        resultToJson=JSONObject.parseObject(result);
        if(null==resultToJson){
            return null;
        }
        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());
        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
            return null;
        }

        accountAmount=getJsonValueByName(retData,QianQiDepositEnum.YOUTU_RESPONSE_FEE_MONOEY.getCode());

        return accountAmount;
    }

    //悠兔理财满标审回调
    @Override
    public boolean youtuLoanSucc(RabbitMessage rabbitMessage){
        log.info("悠兔理财满标审回调start：参数:{}",JSONObject.toJSONString(rabbitMessage));
        boolean flag=false;
        try {
            if (rabbitMessage == null || rabbitMessage.getData() == null) {
                return flag;
            }
            RepaymentFromYoutu repaymentFromYoutu = JSONObject.parseObject(rabbitMessage.getData().toString(), RepaymentFromYoutu.class);
            if (repaymentFromYoutu == null) {
                return flag;
            }

            List<Order> orderList = orderMapper.queryOrderList
                    (Integer.toString(repaymentFromYoutu.getBaikaBorrowId()),
                            Constants.payOrderType.BAIKA_PAY_TYPE,
                            Constants.OrderStatus.WAIT
                    );

            if (orderList == null || orderList.size() == 0) {
                return flag;
            }

            this.youtuCallbackLoanSucc(orderList.get(0).getSerialNo(), repaymentFromYoutu);
        }catch (Exception e){
            log.error("悠兔理财满标审回调异常:",e);
        }
        return flag;
    }

    //悠兔理财流标回调
    @Override
    public boolean youtuLoanCancel(RabbitMessage rabbitMessage){
        log.info("悠兔理财流标回调start：参数:{}",JSONObject.toJSONString(rabbitMessage));
         boolean flag=false;
         try {
             if (rabbitMessage == null || rabbitMessage.getData() == null) {
                 return flag;
             }

             YoutuFailLoanCallback youtuFailLoanCallback = JSONObject.parseObject(rabbitMessage.getData().toString(), YoutuFailLoanCallback.class);
             if (youtuFailLoanCallback == null) {
                 return flag;
             }

             BorrowList borrowList = new BorrowList();
             borrowList.setBorrStatus(BorrowStatusEnum.LOAN_PROCESS.getCode());
             List<BorrowList> borrows = borrowListMapper.select(borrowList);
             if (borrows != null && borrows.size() > 0) {
                 //1.合同修改为取消
                 BorrowList bor = borrows.get(0);
                 Example example = new Example(BorrowList.class);
                 example.createCriteria().andEqualTo("id", youtuFailLoanCallback.getBaikaBorrowId());
                 BorrowList bl = new BorrowList();
                 bl.setBorrStatus(BorrowStatusEnum.CANCEL.getCode());
                 borrowListMapper.updateByExampleSelective(bl, example);
                 //2.退还手续费
                 refundServiceFee(bor.getBorrNum());
                 //3.同步咔咔钱包合同信息
                 AsyncExecutor.execute(new PostAsync<>(borrowListMapper.selectByExample(example), syncACompanyBorrUrl));
             }
         }catch (Exception e){
             log.error("悠兔理财流标回调异常:",e);
         }
         return flag;
    }
    public void refundServiceFee(String borrNum){
        try {
            if(StringUtil.isNotEmpty(borrNum)){
                Map map = new HashMap();
                map.put("borrNum", borrNum);
                map.put("remark", null);
                map.put("operator", null);
                HttpUtils.sendPost(syncACompayRefundUrl,HttpUtils.toParam(map));
            }
        } catch (Exception e) {
            log.error("咔咔钱包退款接口调用失败:",e);
        }
    }

    private String getJsonValueByName(String data,String key){
        String jsonVal=null;
        try{
            JSONObject result=JSONObject.parseObject(data);
            if(result==null||StringUtil.isEmpty(result.getString(key))){
                return null;
            }
            jsonVal=result.getString(key);
        }catch (Exception e){
            log.error("获取json_key值异常",e);
        }
        return jsonVal;
    }
    public static void main(String args[]){

//        ResponseDo responseDo=null;
//        String result=null;
//        JSONObject resultToJson=null;
//        String retCode=null;
//        String retMsg=null;
//        String retData=null;
//        OpenAccountRequest openAccount=new OpenAccountRequest();
//        openAccount.setResponsePath("http://www.baidu.com");
//        openAccount.setCustType(QianQiDepositEnum.CUST_TYPE_LOAN.getCode());
//        openAccount.setRegisterPhone("13501927272");
//        openAccount.setTradeCode(QianQiDepositEnum.TRADE_CODE_OPEN_ACCOUNT.getCode());
//        result= HttpsUtil.sendPost("http://172.16.11.121:6701/page/reqParams", MapUtils.ConvertObjToMap(openAccount));
//                resultToJson=JSONObject.parseObject(result);
//        retData=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_DATA.getCode());
//        responseDo=ResponseDo.newSuccessDo();
//        responseDo.setData(retData);
//        System.out.println();
//        if(StringUtils.isEmpty(result)){
//            log.info("前旗银行存管接口【openAccount】悠兔接口响应数据result:{}",result);
//            responseDo=ResponseDo.newFailedDo("前旗银行开户接口响应数据为空！");
//        }
//        resultToJson=JSONObject.parseObject(result);
//        if(null==resultToJson){
//            responseDo=ResponseDo.newFailedDo("前旗银行开户接口响应数据为空！");
//        }
//        retCode=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_CODE.getCode());
//        retMsg=resultToJson.getString(QianQiDepositEnum.YOUTU_RESPONSE_MSG.getCode());

//        if(!CodeReturn.SUCCESS_CODE.equals(retCode)||StringUtil.isEmpty(retData)){
//            if(StringUtil.isEmpty(retMsg))
//                responseDo=ResponseDo.newFailedDo("前旗银行开户接口异常！");
//            else
//                responseDo=ResponseDo.newFailedDo(retMsg);
//
//        }
//
//        OpenAccountRequest openAccountRequest=JSONObject.parseObject(retData,OpenAccountRequest.class);
//        responseDo=ResponseDo.newSuccessDo();
//        responseDo.setData(openAccountRequest);
//
//        return responseDo;

    }
}
