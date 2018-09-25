package com.jhh.dc.baika.entity.manager_vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金审核详情vo
 */
public class CommissionDetailVo implements Serializable {

    private static final long serialVersionUID = 8951632679596388190L;

    private Integer id;

    private Integer perId;

    @Excel(name = "手机号",width = 20.0D)
    private String phone;

    @Excel(name = "类型",width = 20.0D)
    private String type;

    @Excel(name = "邀请阶段",width = 20.0D)
    private String trackingStatus;

    @Excel(name = "佣金",width = 20.0D)
    private String commission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPerId() {
        return perId;
    }

    public void setPerId(Integer perId) {
        this.perId = perId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
}
