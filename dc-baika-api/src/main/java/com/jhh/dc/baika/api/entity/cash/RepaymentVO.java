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
 * @Description: 还款返回体
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/2 16:53
 * @version: V1.0
 */
@Data
@ApiModel(value = "还款返回体", description = "还款返回体")
public class RepaymentVO {

    @ApiModelProperty(name = "result", value = "响应body")
    private String result;

    @ApiModelProperty(name = "status", value = "true，成功; false，失败")
    private Boolean status;

    @ApiModelProperty(name = "message", value = "响应消息")
    private String message;
}
