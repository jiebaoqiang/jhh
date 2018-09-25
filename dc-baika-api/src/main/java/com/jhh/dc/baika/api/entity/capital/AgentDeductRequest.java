package com.jhh.dc.baika.api.entity.capital;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Description: 代扣传输类
 * @date: 2018/1/19
 */

@Data
@ApiModel(value = "单笔代扣接口请求参数", description = "单笔代扣接口请求参数")
public class AgentDeductRequest implements Serializable {

    private static final long serialVersionUID = 3535485924950867331L;

    @ApiModelProperty(name = "borrNum", value = "合同号", required = true)
    private String borrNum;

    @ApiModelProperty(name = "borrId", value = "合同id", required = true)
    private String borrId;

    @NotNull(message = "optAmount is null")
    @Pattern(regexp = "([1-9]\\d*|0)(\\.\\d{1,2}|\\.)?", message = "optAmount format error")
    @ApiModelProperty(name = "optAmount", value = "操作金额", required = true)
    private String optAmount;

    @ApiModelProperty(name = "bankId", value = "银行id")
    private String bankId;

    @ApiModelProperty(name = "bankName", value = "银行名称", required = true)
    private String bankName;

    @ApiModelProperty(name = "bankCode", value = "银行缩写", required = true)
    private String bankCode;


    @ApiModelProperty(name = "bankNum", value = "银行号码", required = true)
    private String bankNum;

    @ApiModelProperty(name = "description", value = "描述")
    private String description;

    @ApiModelProperty(name = "createUser", value = "操作人")
    private String createUser;

    @ApiModelProperty(name = "collectionUser", value = "催债人")
    private String collectionUser;

    @NotNull(message = "type is null")
    @ApiModelProperty(name = "type", value = "类型：1，提前结清，2，正常还款")
    private String type;

    @ApiModelProperty(name = "triggerStyle", value = "触发机制0,后台触发 ;1,自动触发 ;2,线上修复触发 ;3，用户触发'")
    private String triggerStyle;

    /**支付渠道*/
    private String payChannel;

    //其他传递参数
    private String sid;

    private String guid;

    private String orderNo;

    private String name;

    private String idCardNo;

    private String phone;

    private String serNo;

    private String finalAmount;

    private String subcontractNo;

    private String deductionsType;

    private String banklistId;

    // 验证码
    private String validateCode;

    // 绑卡短信渠道
    private String msgChannel;

    // 支付类型(0 银行卡 1 支付宝)
    private Integer payType;

}
