package com.jhh.dc.baika.entity.bankdeposit;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class BankDepositCommon implements Serializable{
    private static final long serialVersionUID = -1L;

    private String merchantNo;  //商户编号
    private String merOrderNo;   //交易订单号
    private String jsonEnc;     //json加密数据
    private String keyEnc;       //加密公钥
    private String sign;         //加密字符串
    private String url;          //请求地址
}
