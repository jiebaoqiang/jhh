package com.jhh.dc.baika.entity.bankdeposit;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class CheckRegisterPhoneRequest implements Serializable {
    private String phone;//注册手机号

    @Override
    public String toString() {
        return "CheckRegisterPhoneRequest{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
