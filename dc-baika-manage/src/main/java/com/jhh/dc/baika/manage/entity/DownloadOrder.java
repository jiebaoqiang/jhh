package com.jhh.dc.baika.manage.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.jhh.dc.baika.entity.enums.OrderRLStatusEnum;

import java.io.Serializable;

/**
 * 还款流水导出
 */
public class DownloadOrder implements Serializable {
    @Excel(name = "合同编号", width = 20)
    private String borrNum;
    @Excel(name = "还款流水", width = 20)
    private String serialNo;
    @Excel(name = "支付流水", width = 20)
    private String sidNo;
    @Excel(name = "姓名")
    private String username;
    @Excel(name = "身份证号", width = 20)
    private String idCard;
    @Excel(name = "手机号")
    private String customerMobile;
    @Excel(name = "开户行")
    private String bankName;
    @Excel(name = "银行卡号", width = 20)
    private String bankNum;
    @Excel(name = "还款类型")
    private String typeName;
    @Excel(name = "还款状态")
    private String state;
    @Excel(name = "还款金额")
    private String amount;
    @Excel(name = "还款时间", width = 20)
    private String insDate;
    @Excel(name = "操作人")
    private String createUserName;
    @Excel(name = "催收人")
    private String collectionUserName;
    @Excel(name = "备注", width = 40)
    private String remark;
    @Excel(name = "逾期天数")
    private String overdueDays;
    private String borrId;
    private String prodId;
    private String id;
    private String bankId;
    private String perId;
    private String rlState;
    private String bedueDays;
    private String operator;
    private String createUser;
    private String collectionUser;
    private String deductionsType;
    private String type;
    private String typeWithChannel;

    public String getBorrNum() {
        return borrNum;
    }

    public void setBorrNum(String borrNum) {
        this.borrNum = borrNum;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBorrId() {
        return borrId;
    }

    public void setBorrId(String borrId) {
        this.borrId = borrId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getPerId() {
        return perId;
    }

    public void setPerId(String perId) {
        this.perId = perId;
    }

    public String getRlState() {
        return rlState;
    }

    public void setRlState(String rlState) {
        this.rlState = rlState;
        this.state = OrderRLStatusEnum.getDescByCode(rlState);
    }

    public String getBedueDays() {
        return bedueDays;
    }

    public void setBedueDays(String bedueDays) {
        this.bedueDays = bedueDays;
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

    public String getDeductionsType() {
        return deductionsType;
    }

    public void setDeductionsType(String deductionsType) {
        this.deductionsType = deductionsType;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInsDate() {
        return insDate;
    }

    public void setInsDate(String insDate) {
        this.insDate = insDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCollectionUserName() {
        return collectionUserName;
    }

    public void setCollectionUserName(String collectionUserName) {
        this.collectionUserName = collectionUserName;
    }

    public String getSidNo() {
        return sidNo;
    }

    public void setSidNo(String sidNo) {
        this.sidNo = sidNo;
    }
    public String getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(String overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getTypeWithChannel() {
        return typeWithChannel;
    }

    public void setTypeWithChannel(String typeWithChannel) {
        this.typeWithChannel = typeWithChannel;
    }
}
