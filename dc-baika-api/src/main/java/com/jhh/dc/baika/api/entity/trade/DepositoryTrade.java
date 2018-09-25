package com.jhh.dc.baika.api.entity.trade;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 2018/9/7.
 */
@Data
public class DepositoryTrade implements Serializable{

    private static final long serialVersionUID = -1L;

    private String amount;

    private Integer perId;

    private String phone;

    private Integer borrId;

    private String borrNum;

    @ApiModelProperty(name = "triggerStyle", value = " 默认为用户触发 触发机制0,后台触发 ;1,自动触发 ;2,线上修复触发 ;3，用户触发',")
    private Integer triggerStyle = 3;



}
