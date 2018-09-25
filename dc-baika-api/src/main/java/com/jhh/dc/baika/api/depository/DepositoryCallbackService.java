package com.jhh.dc.baika.api.depository;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.bankdeposit.QianQiBack;

/**
 * 2018/9/10. 存管回调
 */
public interface DepositoryCallbackService {

    /**
     *  充值回调
     * @return
     */
    ResponseDo<?> callbackRecharge(QianQiBack back);

    /**
     *  充值回调
     * @return
     */
    ResponseDo<?> callbackWithdrawal(QianQiBack back);

    /**
     *  充值主动查询
     * @param serialNo
     */
    ResponseDo<?> searchRechargeOrder(String serialNo);

    /**
     *  还款主动查询
     * @param serialNo
     * @return
     */
    ResponseDo<?> searchRepayOrder(String serialNo);

    /**
     *  提现主动查询
     * @param serialNo
     * @return
     */
    ResponseDo<?> searchWithdrawalOrder(String serialNo);

    /**
     *  逾期合同同步幼兔
     */
    void depositoryOverdue();
}
