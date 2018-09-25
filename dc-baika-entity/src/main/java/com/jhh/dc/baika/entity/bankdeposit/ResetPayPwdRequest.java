package com.jhh.dc.baika.entity.bankdeposit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPayPwdRequest implements java.io.Serializable{
    private String tradeCode; //业务类型 （无须填写)
    private String custNo;     //前旗银行用户编号
    private String responsePath;//页面回调地址
    private String registerPhone; //注册手机号

    @Override
    public String toString() {
        return "EditPayPwdRequest{" +
                "tradeCode='" + tradeCode + '\'' +
                ", custNo='" + custNo + '\'' +
                ", responsePath='" + responsePath + '\'' +
                ", registerPhone='" + registerPhone + '\'' +
                '}';
    }
}
