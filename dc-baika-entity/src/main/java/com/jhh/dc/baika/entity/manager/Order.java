package com.jhh.dc.baika.entity.manager;

import com.jhh.id.annotation.IdGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "b_loan_order")
@Getter
@Setter
public class Order implements Serializable{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @IdGenerator(value = "dc_baika_loan_order")
    private Integer id;

    private String guid;

    private Integer pId;

    private String serialNo;

    private Integer companyId;

    private Integer perId;

    private Integer bankId;

    private Integer contractId;

    private String optAmount;

    private String actAmount;

    private Date rlDate;

    private String rlRemark;

    private String rlState;

    private String type;

    private String reason;

    private String status;

    private Date creationDate;

    private Date updateDate;

    private String sync;

    private String borrNum;
    private String createUser;
    private String collectionUser;
    private String deductionsType;
    @Transient
    private String realOptAmount;
    @Transient
    private String userPhone;
    @Transient
    private String cardNnum;
    @Transient
    private String userName;
    @Transient
    private String bankNum;

    private Integer payChannel;

    private String channel;

    private String sid;
    @Transient
    private String typeWithChannel;
    private Integer overdueDays;
    private Integer prodId;

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getDeductionsType() {
        return deductionsType;
    }

    public void setDeductionsType(String deductionsType) {
        this.deductionsType = deductionsType;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCollectionUser() {
        return collectionUser;
    }

    public void setCollectionUser(String collectionUser) {
        this.collectionUser = collectionUser;
    }
    
    public String getBorrNum() {
		return borrNum;
	}

	public void setBorrNum(String borrNum) {
		this.borrNum = borrNum;
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
        this.guid = guid;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public String getOptAmount() {
        return optAmount;
    }

    public void setOptAmount(String optAmount) {
        this.optAmount = optAmount;
    }

    public String getActAmount() {
        return actAmount;
    }

    public void setActAmount(String actAmount) {
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
        this.rlRemark = rlRemark;
    }

    public String getRlState() {
        return rlState;
    }

    public void setRlState(String rlState) {
        this.rlState = rlState;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        this.sync = sync;
    }

    public String getRealOptAmount() {
        return realOptAmount;
    }

    public void setRealOptAmount(String realOptAmount) {
        this.realOptAmount = realOptAmount;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCardNnum() {
        return cardNnum;
    }

    public void setCardNnum(String cardNnum) {
        this.cardNnum = cardNnum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTypeWithChannel() {
        return typeWithChannel;
    }

    public void setTypeWithChannel(String typeWithChannel) {
        this.typeWithChannel = typeWithChannel;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", pId=" + pId +
                ", serialNo='" + serialNo + '\'' +
                ", companyId=" + companyId +
                ", perId=" + perId +
                ", bankId=" + bankId +
                ", contractId=" + contractId +
                ", optAmount='" + optAmount + '\'' +
                ", actAmount='" + actAmount + '\'' +
                ", rlDate=" + rlDate +
                ", rlRemark='" + rlRemark + '\'' +
                ", rlState='" + rlState + '\'' +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", sync='" + sync + '\'' +
                ", borrNum='" + borrNum + '\'' +
                ", createUser='" + createUser + '\'' +
                ", collectionUser='" + collectionUser + '\'' +
                ", deductionsType='" + deductionsType + '\'' +
                ", realOptAmount='" + realOptAmount + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", cardNnum='" + cardNnum + '\'' +
                ", userName='" + userName + '\'' +
                ", bankNum='" + bankNum + '\'' +
                ", overdueDays='" + overdueDays + '\'' +
                '}';
    }
}