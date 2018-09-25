package com.jhh.dc.baika.entity.bankdeposit;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenAccountRequest implements java.io.Serializable{
    private String tradeCode;      //业务类型
    private String responsePath;   //页面回调地址
    private String registerPhone;  //注册手机号
    private String custType;        //客户类型

    @Override
    public String toString() {
        return "OpenAccountRequest{" +
                "tradeCode='" + tradeCode + '\'' +
                ", responsePath='" + responsePath + '\'' +
                ", registerPhone='" + registerPhone + '\'' +
                ", custType='" + custType + '\'' +
                '}';
    }
}
