package com.jhh.dc.baika.api.entity.capital;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LoanOrderVO implements Serializable {
    private Integer id;

    private String guid;

    private Integer pId;

    private String borrNum;

    private String serialNo;

    private Integer companyId;

    private Integer perId;

    private Integer bankId;

    private Integer contractId;

    private BigDecimal optAmount;

    private BigDecimal actAmount;

    private Date rlDate;

    private String rlRemark;

    private String rlState;

    private String type;

    private String reason;

    private String status;

    private Date creationDate;

    private Date updateDate;

    private String sync;

    private String createUser;

    private String collectionUser;

    private Integer deductionsType;

    private Integer version;

    private String sid;

    private Integer payChannel;

    private Integer overdueDays;

    private static final long serialVersionUID = 1L;

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid == null ? null : guid.trim();
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getBorrNum() {
        return borrNum;
    }

    public void setBorrNum(String borrNum) {
        this.borrNum = borrNum == null ? null : borrNum.trim();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getPerId() {
        return perId;
    }

    public void setPerId(Integer perId) {
        this.perId = perId;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public BigDecimal getOptAmount() {
        return optAmount;
    }

    public void setOptAmount(BigDecimal optAmount) {
        this.optAmount = optAmount;
    }

    public BigDecimal getActAmount() {
        return actAmount;
    }

    public void setActAmount(BigDecimal actAmount) {
        this.actAmount = actAmount;
    }

    public Date getRlDate() {
        return rlDate;
    }

    public void setRlDate(Date rlDate) {
        this.rlDate = rlDate;
    }

    public String getRlRemark() {
        return rlRemark;
    }

    public void setRlRemark(String rlRemark) {
        this.rlRemark = rlRemark == null ? null : rlRemark.trim();
    }

    public String getRlState() {
        return rlState;
    }

    public void setRlState(String rlState) {
        this.rlState = rlState == null ? null : rlState.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync == null ? null : sync.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getCollectionUser() {
        return collectionUser;
    }

    public void setCollectionUser(String collectionUser) {
        this.collectionUser = collectionUser == null ? null : collectionUser.trim();
    }

    public Integer getDeductionsType() {
        return deductionsType;
    }

    public void setDeductionsType(Integer deductionsType) {
        this.deductionsType = deductionsType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid == null ? null : sid.trim();
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", guid=").append(guid);
        sb.append(", pId=").append(pId);
        sb.append(", borrNum=").append(borrNum);
        sb.append(", serialNo=").append(serialNo);
        sb.append(", companyId=").append(companyId);
        sb.append(", perId=").append(perId);
        sb.append(", bankId=").append(bankId);
        sb.append(", contractId=").append(contractId);
        sb.append(", optAmount=").append(optAmount);
        sb.append(", actAmount=").append(actAmount);
        sb.append(", rlDate=").append(rlDate);
        sb.append(", rlRemark=").append(rlRemark);
        sb.append(", rlState=").append(rlState);
        sb.append(", type=").append(type);
        sb.append(", reason=").append(reason);
        sb.append(", status=").append(status);
        sb.append(", creationDate=").append(creationDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", sync=").append(sync);
        sb.append(", createUser=").append(createUser);
        sb.append(", collectionUser=").append(collectionUser);
        sb.append(", deductionsType=").append(deductionsType);
        sb.append(", version=").append(version);
        sb.append(", sid=").append(sid);
        sb.append(", payChannel=").append(payChannel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LoanOrderVO other = (LoanOrderVO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGuid() == null ? other.getGuid() == null : this.getGuid().equals(other.getGuid()))
            && (this.getpId() == null ? other.getpId() == null : this.getpId().equals(other.getpId()))
            && (this.getBorrNum() == null ? other.getBorrNum() == null : this.getBorrNum().equals(other.getBorrNum()))
            && (this.getSerialNo() == null ? other.getSerialNo() == null : this.getSerialNo().equals(other.getSerialNo()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()))
            && (this.getPerId() == null ? other.getPerId() == null : this.getPerId().equals(other.getPerId()))
            && (this.getBankId() == null ? other.getBankId() == null : this.getBankId().equals(other.getBankId()))
            && (this.getContractId() == null ? other.getContractId() == null : this.getContractId().equals(other.getContractId()))
            && (this.getOptAmount() == null ? other.getOptAmount() == null : this.getOptAmount().equals(other.getOptAmount()))
            && (this.getActAmount() == null ? other.getActAmount() == null : this.getActAmount().equals(other.getActAmount()))
            && (this.getRlDate() == null ? other.getRlDate() == null : this.getRlDate().equals(other.getRlDate()))
            && (this.getRlRemark() == null ? other.getRlRemark() == null : this.getRlRemark().equals(other.getRlRemark()))
            && (this.getRlState() == null ? other.getRlState() == null : this.getRlState().equals(other.getRlState()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreationDate() == null ? other.getCreationDate() == null : this.getCreationDate().equals(other.getCreationDate()))
            && (this.getUpdateDate() == null ? other.getUpdateDate() == null : this.getUpdateDate().equals(other.getUpdateDate()))
            && (this.getSync() == null ? other.getSync() == null : this.getSync().equals(other.getSync()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCollectionUser() == null ? other.getCollectionUser() == null : this.getCollectionUser().equals(other.getCollectionUser()))
            && (this.getDeductionsType() == null ? other.getDeductionsType() == null : this.getDeductionsType().equals(other.getDeductionsType()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getSid() == null ? other.getSid() == null : this.getSid().equals(other.getSid()))
            && (this.getPayChannel() == null ? other.getPayChannel() == null : this.getPayChannel().equals(other.getPayChannel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGuid() == null) ? 0 : getGuid().hashCode());
        result = prime * result + ((getpId() == null) ? 0 : getpId().hashCode());
        result = prime * result + ((getBorrNum() == null) ? 0 : getBorrNum().hashCode());
        result = prime * result + ((getSerialNo() == null) ? 0 : getSerialNo().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        result = prime * result + ((getPerId() == null) ? 0 : getPerId().hashCode());
        result = prime * result + ((getBankId() == null) ? 0 : getBankId().hashCode());
        result = prime * result + ((getContractId() == null) ? 0 : getContractId().hashCode());
        result = prime * result + ((getOptAmount() == null) ? 0 : getOptAmount().hashCode());
        result = prime * result + ((getActAmount() == null) ? 0 : getActAmount().hashCode());
        result = prime * result + ((getRlDate() == null) ? 0 : getRlDate().hashCode());
        result = prime * result + ((getRlRemark() == null) ? 0 : getRlRemark().hashCode());
        result = prime * result + ((getRlState() == null) ? 0 : getRlState().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreationDate() == null) ? 0 : getCreationDate().hashCode());
        result = prime * result + ((getUpdateDate() == null) ? 0 : getUpdateDate().hashCode());
        result = prime * result + ((getSync() == null) ? 0 : getSync().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCollectionUser() == null) ? 0 : getCollectionUser().hashCode());
        result = prime * result + ((getDeductionsType() == null) ? 0 : getDeductionsType().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getSid() == null) ? 0 : getSid().hashCode());
        result = prime * result + ((getPayChannel() == null) ? 0 : getPayChannel().hashCode());
        return result;
    }
}