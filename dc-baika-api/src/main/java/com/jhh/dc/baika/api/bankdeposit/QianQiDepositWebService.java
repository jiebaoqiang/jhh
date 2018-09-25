package com.jhh.dc.baika.api.bankdeposit;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app_vo.RabbitMessage;
import com.jhh.dc.baika.entity.bankdeposit.*;

public interface QianQiDepositWebService {

    /**
     * 判断用户是否允许注册true允许  false不允许
     * @param registerPhone
     * @return
     */
    boolean checkIsAgreeRegister(String registerPhone);
    /**
     * 前旗存管开户接口
     * @param registerPhone
     * @param webResponseUrl
     * @return
     */
    ResponseDo<BankDepositCommon> openAccount(String registerPhone, String webResponseUrl);

    /**
     * 前旗存管授权接口
     * @param custAuthRequest
     * @return
     */

    ResponseDo<BankDepositCommon> custAuth(CustAuthRequest custAuthRequest);

    /**
     * 前旗存管修改密码接口
     * @param editPayPwdRequest
     * @return
     */
    ResponseDo<BankDepositCommon> editPayPwd(EditPayPwdRequest editPayPwdRequest);

    /**
     * 前旗银行重置密码接口
     * @param resetPayPwdRequest
     * @return
     */
    ResponseDo<BankDepositCommon> resetPayPwd(ResetPayPwdRequest resetPayPwdRequest);
    /**
     * 前旗存管充值接口
     * @return
     */
    ResponseDo<BankDepositCommon> 	reCharge(RechargeRequest rechargeRequest);

    /**
     * 前旗存管提现接口
     * @param withdrawRequest
     * @return
     */
    ResponseDo<BankDepositCommon> withDraw(WithdrawRequest withdrawRequest);

    /**
     * 前旗存管查询账户余额接口
     */
    String queryUserAccountAmount(String registerPhone);

    /**
     * 前旗查询充值或提现手续费接口
     * @return
     */
    String queryWithdrawCharge();


    /**
     * 满标审通过代码
     */
    boolean youtuLoanSucc(RabbitMessage rabbitMessage);

    /**
     * 流标
     */
    boolean youtuLoanCancel(RabbitMessage rabbitMessage);

}
