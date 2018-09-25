package com.jhh.dc.baika.common.enums;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.common.enums
 * @Description: 支付渠道枚举
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/19 16:27
 * @version: V1.0
 */
public enum PayChannelEnum {

    JHH_YSB(0,"金互行支付中心-银生宝 "),YSB(1,"银生宝");

    private Integer code;
    private String desc;

    PayChannelEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDescByCode(String code){
        for (PayChannelEnum payChannelEnum:PayChannelEnum.values()){
            if(payChannelEnum.code.equals(code)){
                return payChannelEnum.desc;
            }
        }
        return null;
    }
}
