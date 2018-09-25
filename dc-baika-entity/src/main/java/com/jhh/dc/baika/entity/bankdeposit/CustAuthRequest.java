package com.jhh.dc.baika.entity.bankdeposit;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustAuthRequest implements java.io.Serializable{
    private String tradeCode;      //业务类型 (无须填写)
    private String custNo;         //用户号
    private String authType;       //授权类型
    private String expiryTime;     //授权有效期(无须填写)
    private String amount;          //授权最大单笔交易金额(无须填写)
    private String responsePath;   //授权页面回调地址
    private String registerPhone;  //注册手机号

    @Override
    public String toString() {
        return "custAuthRequest{" +
                "tradeCode='" + tradeCode + '\'' +
                ", custNo='" + custNo + '\'' +
                ", authType='" + authType + '\'' +
                ", expiryTime='" + expiryTime + '\'' +
                ", amount='" + amount + '\'' +
                ", responsePath='" + responsePath + '\'' +
                ", registerPhone='" + registerPhone + '\'' +
                '}';
    }
}
