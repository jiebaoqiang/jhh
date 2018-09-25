package com.jhh.dc.baika.api.depository;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.trade.DepositoryTrade;
import com.jhh.dc.baika.entity.bankdeposit.BankDepositCommon;

/**
 * 2018/9/7. 存管交易相关api
 */
public interface DepositoryPayService {

    /**
     *  充值
     * @param trade 统一参数
     * @return
     */
    ResponseDo<BankDepositCommon> depositoryRecharge(DepositoryTrade trade);

    /**
     *  提现
     * @param withdrawal
     * @return
     */
    ResponseDo<BankDepositCommon> depositoryWithdrawal(DepositoryTrade withdrawal,String fee);

    /**
     *  还款 未逾期存管还款
     * @return
     */
    ResponseDo<?> depositoryRepay(DepositoryTrade trade);

}
