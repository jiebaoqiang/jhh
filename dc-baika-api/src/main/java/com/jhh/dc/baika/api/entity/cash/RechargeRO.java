package com.jhh.dc.baika.api.entity.cash;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.api.entity.cash
 * @Description: 充值请求体
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/2 16:51
 * @version: V1.0
 */

@Data
@ApiModel(value = "充值接口请求参数", description = "充值接口请求参数")
public class RechargeRO {

    @NotBlank(message = "userId不能为空")
    @ApiModelProperty(name = "userId", value = "用户唯一标识",required = true)

    private String userId;

    @NotBlank(message = "充值金额不能为空")
    @Pattern(regexp = "[1-9][0-9]*",message = "充值金额必须为正整数")
    @ApiModelProperty(name = "amount", value = "充值金额",required = true)
    private String amount;

    @NotBlank(message = "充值方式不能为空")
    @ApiModelProperty(name = "rechargeBizType", value = "充值方式01网银充值,02：快捷充值",required = true)
    private String rechargeBizType;
}
