package com.jhh.dc.baika.entity.callback;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 2018/6/5.
 */
@Data
public class LKLBatchCallback implements Serializable{

    private static final long serialVersionUID = 6001391588554634658L;
    /**
     * 状态
     */
    private String state;

    /**
     * 信息
     */
    private String msg;

    /**
     *  错误订单存放
     */
    private Map<String, Object> extension;

    /**
     * 支付中心流水号
     */
    private String sid;

    /**
     *  支付渠道
     */
    private String channelKey;
}
