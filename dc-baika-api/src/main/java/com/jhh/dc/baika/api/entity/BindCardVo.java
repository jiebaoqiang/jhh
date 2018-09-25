package com.jhh.dc.baika.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 2018/4/18.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindCardVo implements Serializable{

    private static final long serialVersionUID = 1958627421958249784L;
    @NotNull
    private String per_id;
    @NotNull
    private String bankName;
    @NotNull
    private String bank_num;
    @NotNull
    private String bankCode;
    @NotNull
    private String phone;

    private String status;

    private String tokenKey;

    @NotNull
    private String device;

    private String validateCode;

    private String extension;

    private String channel;

    private String msgChannel;

    private String orderNo;


}
