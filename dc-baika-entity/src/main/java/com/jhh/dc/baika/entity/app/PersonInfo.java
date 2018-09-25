package com.jhh.dc.baika.entity.app;

import java.io.Serializable;

public class PersonInfo implements Serializable {
    private Integer id;

    private Integer perId;

    private String debtInfo;

    private String liveInfo;

    private String transInfo;

    private String department;

    private String job;

    private String companyInfo;

    private String familyLink1;

    private String relationship1;

    private String phone1;

    private String cardNum1;

    private String familyLink2;

    private String relationship2;

    private String phone2;

    private static final long serialVersionUID = 1L;

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

    public String getDebtInfo() {
        return debtInfo;
    }

    public void setDebtInfo(String debtInfo) {
        this.debtInfo = debtInfo == null ? null : debtInfo.trim();
    }

    public String getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(String liveInfo) {
        this.liveInfo = liveInfo == null ? null : liveInfo.trim();
    }

    public String getTransInfo() {
        return transInfo;
    }

    public void setTransInfo(String transInfo) {
        this.transInfo = transInfo == null ? null : transInfo.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo == null ? null : companyInfo.trim();
    }

    public String getFamilyLink1() {
        return familyLink1;
    }

    public void setFamilyLink1(String familyLink1) {
        this.familyLink1 = familyLink1 == null ? null : familyLink1.trim();
    }

    public String getRelationship1() {
        return relationship1;
    }

    public void setRelationship1(String relationship1) {
        this.relationship1 = relationship1 == null ? null : relationship1.trim();
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1 == null ? null : phone1.trim();
    }

    public String getCardNum1() {
        return cardNum1;
    }

    public void setCardNum1(String cardNum1) {
        this.cardNum1 = cardNum1 == null ? null : cardNum1.trim();
    }

    public String getFamilyLink2() {
        return familyLink2;
    }

    public void setFamilyLink2(String familyLink2) {
        this.familyLink2 = familyLink2 == null ? null : familyLink2.trim();
    }

    public String getRelationship2() {
        return relationship2;
    }

    public void setRelationship2(String relationship2) {
        this.relationship2 = relationship2 == null ? null : relationship2.trim();
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2 == null ? null : phone2.trim();
    }
}