package com.jhh.dc.baika.entity.bankdeposit;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryWithdrawRequest implements java.io.Serializable{

    private String feeCode;      //费用类型WITHDRAW_FEE
    private String identityType;     //资产类型 1 白卡

    @Override
    public String toString() {
        return "queryWithdrawRequest{" +
                "feeCode='" + feeCode + '\'' +
                ", identityType='" + identityType + '\'' +
                '}';
    }
}
