package com.jhh.dc.baika.app.web.logic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jhh.dc.baika.api.bankdeposit.QianQiDepositBackService;
import com.jhh.dc.baika.api.depository.DepositoryCallbackService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.entity.bankdeposit.QianQiBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2018/9/10. 存管回调接口
 */
@Slf4j
@RestController
@RequestMapping("/callback")
public class CallBackController {

    @Reference
    private DepositoryCallbackService depositoryCallbackService;

    @Reference
    private QianQiDepositBackService mQianQiDepositBackService;

    /**
     *  充值回调
     * @return
     */
    @RequestMapping("/recharge")
    public String callbackRecharge(@RequestBody QianQiBack qianQiBack){
        ResponseDo<?> responseDo = depositoryCallbackService.callbackRecharge(qianQiBack);
        if (CodeReturn.success == responseDo.getCode()){
            return "OK";
        }else {
            return "NO";
        }
    }

    /**
     *  提现回调
     * @return
     */
    @RequestMapping("/withdraw")
    public String callbackWithdrawal(@RequestBody QianQiBack qianQiBack){
        ResponseDo<?> responseDo = depositoryCallbackService.callbackWithdrawal(qianQiBack);
        if (CodeReturn.success == responseDo.getCode()){
            return "OK";
        }else {
            return "NO";
        }
    }

    /**
     * 开户注册回调
     * @param qianQiBack
     * @return
     */
    @RequestMapping("/accountBack")
    public String accountBack(@RequestBody QianQiBack qianQiBack){
        log.info("进入开户回调:"+qianQiBack);
        return mQianQiDepositBackService.accountBack(qianQiBack);

    }

    /**
     * 授权回调
     * @param qianQiBack
     * @return
     */
    @RequestMapping("/authBack")
    public String authBack(@RequestBody QianQiBack qianQiBack){
        log.info("进入授权回调:"+qianQiBack);
        return mQianQiDepositBackService.authBack(qianQiBack);
    }
}
