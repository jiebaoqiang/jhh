package com.jhh.dc.baika.api.channel;

import com.jhh.dc.baika.api.entity.cash.WithdrawalVo;
import com.jhh.dc.baika.api.entity.ResponseDo;

/**
 * 提现相关接口
 */
public interface WithdrawalService {

    /**
     *  佣金提现
     * @return 响应结果
     */
    ResponseDo<Integer> getCommissionWithdrawal(WithdrawalVo vo);

    /**
     *  佣金提现结果查询
     * @param serNo
     * @return
     */
    ResponseDo<?> commissionState(String serNo);
}
