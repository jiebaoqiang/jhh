package com.jhh.dc.baika.api.channel;

import com.jhh.dc.baika.api.entity.capital.AgentPayRequest;
import com.jhh.dc.baika.api.entity.ResponseDo;

/**
 * 2018/3/30.
 */
public interface AgentPayService {
    
    ResponseDo<?> pay(AgentPayRequest pay);

}
