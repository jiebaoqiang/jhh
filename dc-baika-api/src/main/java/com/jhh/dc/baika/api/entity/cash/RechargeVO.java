package com.jhh.dc.baika.api.entity.cash;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.api.entity.cash
 * @Description: 充值返回体
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/2 16:51
 * @version: V1.0
 */

@Data
@ApiModel(value = "充值返回体", description = "充值返回体")
public class RechargeVO {
    @ApiModelProperty(name = "result", value = "充值url")
    private String result;

    @ApiModelProperty(name = "status", value = "状态 true获取成功")
    private Boolean status;

    @ApiModelProperty(name = "message", value = "响应消息")
    private String message;

}
