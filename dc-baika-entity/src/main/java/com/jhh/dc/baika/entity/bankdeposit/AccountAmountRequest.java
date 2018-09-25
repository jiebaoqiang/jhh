package com.jhh.dc.baika.entity.bankdeposit;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountAmountRequest implements java.io.Serializable{
    private String tradeCode;      //业务类型 (无须填写)
    private String registerPhone;  //注册手机号


    @Override
    public String toString() {
        return "custAuthRequest{" +
                "tradeCode='" + tradeCode + '\'' +
                ", registerPhone='" + registerPhone + '\'' +
                '}';
    }
}
