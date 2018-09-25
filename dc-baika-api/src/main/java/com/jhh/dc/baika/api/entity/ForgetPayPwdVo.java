package com.jhh.dc.baika.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 2018/7/10.
 */
@Data
@ApiModel(value = "忘记密码请求参数")
public class ForgetPayPwdVo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "phone", value = "手机号", required = true)
    private String phone;
    @ApiModelProperty(name = "graphiCode", value = "图形码", required = true)
    private String graphiCode;
    @ApiModelProperty(name = "validateCode", value = "验证码", required = true)
    private String validateCode;
    @ApiModelProperty(name = "timestamp", value = "时间戳", required = true)
    private String timestamp;

    @ApiModelProperty(name = "returnUrl", value = "返回地址")
    private String returnUrl;

}
