package com.jhh.dc.baika.api.channel;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.AgentDeductBatchRequest;
import com.jhh.dc.baika.api.entity.capital.AgentDeductRequest;

/**
 *  代扣
 */
public interface AgentDeductService {
    
     ResponseDo<?> deduct(AgentDeductRequest request);

     ResponseDo<?> deductBatch(AgentDeductBatchRequest requests);


}
