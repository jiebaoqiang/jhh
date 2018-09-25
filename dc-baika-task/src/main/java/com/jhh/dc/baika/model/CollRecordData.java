package com.jhh.dc.baika.model;


public class CollRecordData {
    //collectors_list表ID
    private String contractId;
    //催款人
    private String bedueUser;
    //操作人
    private String createUser;
    //操作时间
    private String sysTime;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getBedueUser() {
        return bedueUser;
    }

    public void setBedueUser(String bedueUser) {
        this.bedueUser = bedueUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getSysTime() {
        return sysTime;
    }

    public void setSysTime(String sysTime) {
        this.sysTime = sysTime;
    }
}
