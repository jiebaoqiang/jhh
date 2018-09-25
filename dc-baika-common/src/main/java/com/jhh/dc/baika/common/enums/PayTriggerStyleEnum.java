package com.jhh.dc.baika.common.enums;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.common.enums
 * @Description: 支付触发方式
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/22 10:52
 * @version: V1.0
 */
public enum PayTriggerStyleEnum {

    BACK_GROUND(0,"后台管理人员"),AUTO(1,"任务自动触发"),BUG_FIX(2,"线上bug修复"),USER_TRIGGER(3,"用户触发");

    private Integer code;
    private String desc;

    PayTriggerStyleEnum(Integer code, String desc){
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
        for (PayTriggerStyleEnum payTriggerStyleEnum:PayTriggerStyleEnum.values()){
            if(payTriggerStyleEnum.code.equals(code)){
                return payTriggerStyleEnum.desc;
            }
        }
        return null;
    }
}
