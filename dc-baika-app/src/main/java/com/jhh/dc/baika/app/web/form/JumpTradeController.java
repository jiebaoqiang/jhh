package com.jhh.dc.baika.app.web.form;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.api.app.LoanService;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.depository.DepositoryPayService;
import com.jhh.dc.baika.api.entity.*;
import com.jhh.dc.baika.api.entity.trade.DepositoryTrade;
import com.jhh.dc.baika.app.common.exception.CommonException;
import com.jhh.dc.baika.app.web.exception.ExceptionPageController;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.entity.bankdeposit.BankDepositCommon;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 2018/7/23.
 */
@Controller
@RequestMapping("/form")
@Slf4j
public class JumpTradeController extends ExceptionPageController {

    @Reference
    private UserService userService;

    @Reference
    private LoanService loanService;

    @Reference
    private DepositoryPayService depositoryPayService;



    @ApiOperation(value = "跳转还款页面",notes = "跳转还款页面",hidden = true)
    @RequestMapping("/jumpRepay/{perId}/{borrId}")
    public String jumpRepay(@PathVariable("perId") String perId, @PathVariable("borrId") String borrId,Map<String,Object> map) throws CommonException {
        ResponseDo<RepayInfoDo> bankResponseDo = loanService.jumpRepay(perId, borrId);
        if (bankResponseDo != null && 200 == bankResponseDo.getCode()){
            map.put("info",bankResponseDo.getData());
        }else {
            throw new CommonException(201,bankResponseDo == null ? "服务器开小差了，请稍候再试" : bankResponseDo.getInfo());
        }
        return "trade/repay";
    }

    @ApiOperation(value = "付款页面",notes = "跳转付款页面",hidden = true)
    @RequestMapping("/paymentInfo")
    public String payment(PaymentInfoVo vo, Map<String,Object> map){
        ResponseDo<PaymentInfoDo> paymentInfo = loanService.paymentInfo(vo);
        if (200 == paymentInfo.getCode()){
            map.put("info",paymentInfo.getData());
        }
        return "trade/payment";
    }

    @ApiOperation(value = "充值", notes = "用户充值")
    @RequestMapping(value = "/recharge")
    public String recharge(DepositoryTrade recharge,Map<String,Object> map) throws CommonException {
        log.info("\n=========== 充值接口 come in ===========");
        log.info("\n前端请求数据:{}", recharge.toString());
        ResponseDo<BankDepositCommon> responseDo = depositoryPayService.depositoryRecharge(recharge);
        if (CodeReturn.success == responseDo.getCode()){
            map.put("recharge",responseDo.getData());
            return "trade/recharge";
        }else {
            throw new CommonException(CodeReturn.fail,responseDo.getInfo());
        }
    }

    @ApiOperation(value = "跳转提现页面",notes = "跳转提现页面",hidden = true)
    @RequestMapping("/jumpWithdrawal/{perId}")
    public String jumpWithdrawal(@PathVariable("perId") String perId, String borrId,Map<String,Object> map) throws CommonException {
        if(StringUtils.isEmpty(borrId)){
            throw new CommonException(201,"余额不足，无法提现");
        }
        ResponseDo<DepositDo> bankResponseDo = loanService.jumpWithdrawal(perId, borrId);
        if (bankResponseDo != null && 200 == bankResponseDo.getCode()){
            map.put("info",bankResponseDo.getData());
        }else {
            log.info(bankResponseDo.getInfo());
            throw new CommonException(201,bankResponseDo == null ? "服务器开小差了，请稍候再试" : bankResponseDo.getInfo());
        }
        return "trade/deposit";
    }

    @ApiOperation(value = "提现", notes = "用户提现")
    @RequestMapping(value = "/withdrawal")
    public String withdrawal(DepositoryTrade withdrawal,String fee, Map<String,Object> map) throws CommonException {
        log.info("\n=========== 提现接口 come in ===========");
        log.info("\n前端请求数据:{}", withdrawal.toString());
        ResponseDo<BankDepositCommon> responseDo = depositoryPayService.depositoryWithdrawal(withdrawal,fee);
        if (CodeReturn.success == responseDo.getCode()){
            map.put("withdraw",responseDo.getData());
            return "trade/withdraw";
        }else {
            throw new CommonException(CodeReturn.fail,responseDo.getInfo());
        }
    }

    @ApiOperation(value = "支付处理中",notes = "支付成功处理中页面",hidden = true)
    @RequestMapping("/paying/{registerPhone}")
    public String feedback(@PathVariable("registerPhone") String registerPhone, Map<String,Object> map) {
        map.put("registerPhone",registerPhone);
        return "status/paying";
    }

    @ApiOperation(value = "提现处理中",notes = "提现处理中页面",hidden = true)
    @RequestMapping("/fail")
    public String fail(Map<String,Object> map) {
        return "status/fail";
    }

}
