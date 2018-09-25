package com.jhh.dc.baika.entity.share_vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Setter
@Getter
public class CommissionOrderVO implements Serializable {

    private static final long serialVersionUID = -1009861124083622856L;

    private int id;

    private int perId;

    private String deviceType;

    private String inviterPhone;

    private int channel;

    private String channelPhone;

    private String tracking;

    private int ruleId;

    private int triggerGroup;

    private int inviterLevel;

    private int trackingStatus;

    private int reviewId;

    private int withdrawStatus;

    private BigDecimal commissionAmount;

    private Date creationDate;

    private Date updateDate;
}
