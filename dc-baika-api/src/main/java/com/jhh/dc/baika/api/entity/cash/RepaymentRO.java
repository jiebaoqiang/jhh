package com.jhh.dc.baika.api.entity.cash;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.api.entity.cash
 * @Description: 还款请求体
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/2 16:53
 * @version: V1.0
 */

@Data
@ApiModel(value = "还款接口请求参数", description = "还款接口请求参数")
public class RepaymentRO {

    @NotBlank(message = "userId不能为空")
    @ApiModelProperty(name = "userId", value = "用户唯一标识",required = true)
    private String userId;

    @NotNull(message = "platformProductsId不能为空")
    @ApiModelProperty(name = "platformProductsId", value = "产品唯一标识",required = true)
    private Long platformProductsId;

    @NotNull(message = "repayType不能为空")
    @ApiModelProperty(name = "repayType", value = "还款类型0提前还款;1常规还款(到期还款,预期还款)",required = true)
    private Integer repayType;





}
