package com.jhh.dc.baika.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @auther: wenfucheng
 * @date: 2018/9/6 14:19
 * @description: 个人信息提交实体类
 */
@Data
public class PersonInfoVO implements Serializable {

    @ApiModelProperty(name = "perId", value = "用户id", required = true)
    private Integer perId;

    @ApiModelProperty(name = "debtInfo", value = "个人负债情况", required = true)
    private String debtInfo;

    @ApiModelProperty(name = "liveInfo", value = "居住情况", required = true)
    private String liveInfo;

    @ApiModelProperty(name = "transInfo", value = "车辆情况", required = true)
    private String transInfo;

    @ApiModelProperty(name = "department", value = "所在部门", required = true)
    private String department;

    @ApiModelProperty(name = "job", value = "担任职务", required = true)
    private String job;

    @ApiModelProperty(name = "companyInfo", value = "单位性质", required = true)
    private String companyInfo;

    @ApiModelProperty(name = "familyLink1", value = "家庭联系人1", required = true)
    private String familyLink1;

    @ApiModelProperty(name = "relationship1", value = "关系1", required = true)
    private String relationship1;

    @ApiModelProperty(name = "phone1", value = "联系电话1", required = true)
    private String phone1;

    @ApiModelProperty(name = "cardNum1", value = "身份证号1", required = true)
    private String cardNum1;

    @ApiModelProperty(name = "familyLink2", value = "家庭联系人2", required = true)
    private String familyLink2;

    @ApiModelProperty(name = "relationship2", value = "关系2", required = true)
    private String relationship2;

    @ApiModelProperty(name = "phone2", value = "联系电话2", required = true)
    private String phone2;
}
