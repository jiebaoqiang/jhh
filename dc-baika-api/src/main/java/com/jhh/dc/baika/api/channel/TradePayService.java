package com.jhh.dc.baika.api.channel;

import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.TradeBatchVo;
import com.jhh.dc.baika.api.entity.capital.TradeVo;

/**
 *  此接口只做代付代扣调用第三方 没有任何逻辑加入
 */
public interface TradePayService {

    ResponseDo<String> postPayment(TradeVo vo) throws Exception;

    ResponseDo<String> postDeduct(TradeVo vo) throws Exception;

    ResponseDo<?> state(String serialNo);

    ResponseDo<?> batchDeduct(TradeBatchVo vo);

    /**
     * 退款服务
     */
    ResponseDo<String> refund(TradeVo tradeVo) throws Exception;
}
