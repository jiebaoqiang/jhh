package com.jhh.dc.baika.api.entity.capital;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.entity.capital
 * @Description: 代付
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/19 13:48
 * @version: V1.0
 */

@Data
@ApiModel(value = "代付接口请求参数", description = "代付接口请求参数")
public class AgentpayRO implements Serializable{

    private static final long serialVersionUID = 3535485924950867331L;
    @NotNull(message = "triggerStyle不能为空")
    @ApiModelProperty(name = "triggerStyle", value = "0,后台触发 ;1,自动触发 ;2,线上修复触发 ;3，用户触发",required = true)
    private Integer triggerStyle;

    @NotNull(message = "borrowId不能为空")
    @ApiModelProperty(name = "borrowId", value = "借款单id",required = true)
    private String borrowId;

    @NotNull(message = "userId不能为空")
    @ApiModelProperty(name = "userId", value = "app端用户唯一标",required = true)
    private String userId;



}
