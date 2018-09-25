package com.jhh.dc.baika.api.entity.capital;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Description: 代扣传输类
 * @date: 2018/1/19
 */

@Data
@ApiModel(value = "批量代扣接口请求参数", description = "批量代扣接口请求参数")
public class AgentDeductBatchRequest implements Serializable {

    private static final long serialVersionUID = 3535485924950867331L;

    @NotNull(message = "deductSize")
    @ApiModelProperty(name = "deductSize", value = "代扣对象数量", required = true)
    private Integer deductSize;

    @NotNull(message = "payChannel")
    @ApiModelProperty(name = "payChannel", value = "交易渠道")
    private String payChannel;

    @NotNull(message = "optPerson")
    @ApiModelProperty(name = "optPerson", value = "批次操作人")
    private String optPerson;

    @ApiModelProperty(name = "triggerStyle", value = "触发机制0,后台触发 ;1,自动触发 ;2,线上修复触发 ;3，用户触发'")
    private String triggerStyle;

    @NotNull(message = "requests")
    @ApiModelProperty(name = "requests", value = "代扣对象集合")
    List<AgentDeductRequest> requests;

}
