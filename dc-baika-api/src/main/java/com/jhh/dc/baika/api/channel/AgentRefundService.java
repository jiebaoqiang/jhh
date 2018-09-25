package com.jhh.dc.baika.api.channel;


import com.jhh.dc.baika.api.entity.capital.AgentRefundRequest;
import com.jhh.dc.baika.api.entity.ResponseDo;

/**
 * 退款服务
 */
public interface AgentRefundService {

    /**
     * 退款
     */
    ResponseDo<?> refund(AgentRefundRequest refund);

}
