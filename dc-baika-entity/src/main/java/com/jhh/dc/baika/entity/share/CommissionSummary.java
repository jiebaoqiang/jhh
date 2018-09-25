package com.jhh.dc.baika.entity.share;

import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "commission_summary")
public class CommissionSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer perId;

    @Excel(name="手机号码",width = 20)
    private String phone;

    private Integer inviterId;

    @Excel(name="邀请我的人",width = 20)
    private String inviterPhone;

    @Excel(name="佣金总额",width = 20)
    private BigDecimal commissionTotal;

    @Excel(name="未领佣金",width = 20)
    private BigDecimal commissionBalance;

    @Excel(name="一级邀请人数",width = 20)
    private Integer inviterLevel1Count;

    @Excel(name="二级邀请人数",width = 20)
    private Integer inviterLevel2Count;

    private Date updateDate;

    private Integer version;

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

    public Integer getInviterId() {
        return inviterId;
    }

    public void setInviterId(Integer inviterId) {
        this.inviterId = inviterId;
    }

    public String getInviterPhone() {
        return inviterPhone;
    }

    public void setInviterPhone(String inviterPhone) {
        this.inviterPhone = inviterPhone;
    }

    public BigDecimal getCommissionTotal() {
        return commissionTotal;
    }

    public void setCommissionTotal(BigDecimal commissionTotal) {
        this.commissionTotal = commissionTotal;
    }

    public BigDecimal getCommissionBalance() {
        return commissionBalance;
    }

    public void setCommissionBalance(BigDecimal commissionBalance) {
        this.commissionBalance = commissionBalance;
    }

    public Integer getInviterLevel1Count() {
        return inviterLevel1Count;
    }

    public void setInviterLevel1Count(Integer inviterLevel1Count) {
        this.inviterLevel1Count = inviterLevel1Count;
    }

    public Integer getInviterLevel2Count() {
        return inviterLevel2Count;
    }

    public void setInviterLevel2Count(Integer inviterLevel2Count) {
        this.inviterLevel2Count = inviterLevel2Count;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CommissionSummary{");
        sb.append("id=").append(id);
        sb.append(", perId=").append(perId);
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", inviterId=").append(inviterId);
        sb.append(", inviterPhone='").append(inviterPhone).append('\'');
        sb.append(", commissionTotal=").append(commissionTotal);
        sb.append(", commissionBalance=").append(commissionBalance);
        sb.append(", inviterLevel1Count=").append(inviterLevel1Count);
        sb.append(", inviterLevel2Count=").append(inviterLevel2Count);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", version=").append(version);
        sb.append('}');
        return sb.toString();
    }
}