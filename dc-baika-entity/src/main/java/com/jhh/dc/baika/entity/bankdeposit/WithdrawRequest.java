package com.jhh.dc.baika.entity.bankdeposit;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawRequest implements java.io.Serializable{

    private String tradeCode;      //业务类型
    private String acctNo;     //账户号
    private String amount;     //提现金额
    private String responsePath;//页面回调地址
    private String registerPhone;//注册手机号
    private String baikaOrderNo;//交易流水号
    private String incomeAmt;//手续费

    @Override
    public String toString() {
        return "WithdrawRequest{" +
                "tradeCode='" + tradeCode + '\'' +
                ", acctNo='" + acctNo + '\'' +
                ", amount='" + amount + '\'' +
                ", responsePath='" + responsePath + '\'' +
                ", registerPhone='" + registerPhone + '\'' +
                ", baikaOrderNo='" + baikaOrderNo + '\'' +
                '}';
    }
}
