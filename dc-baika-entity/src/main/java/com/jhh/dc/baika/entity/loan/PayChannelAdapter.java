package com.jhh.dc.baika.entity.loan;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * 2018/6/20.
 */
@Data
@Table(name = "c_pay_channel_adapter")
public class PayChannelAdapter implements Serializable{
    private static final long serialVersionUID = 3375607256955796215L;

    private int id;

    private String payCenterChannel;

    private String type;

    private String channel;

    private String name;

    private int enable;
}
