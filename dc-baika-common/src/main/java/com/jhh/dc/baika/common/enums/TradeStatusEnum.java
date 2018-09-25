package com.jhh.dc.baika.common.enums;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.common.enums
 * @Description: 交易状态
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/19 10:22
 * @version: V1.0
 */
public enum  TradeStatusEnum {
    P("p","处理中"),S("s","成功"),F("f","失败"),Q("q","清结算处理中"),C("c","清结算失败"),M("m","匹配中"),MS("ms","匹配成功");

    private String code;
    private String desc;

    TradeStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDescByCode(String code){
        for (TradeStatusEnum tradeStatusEnum:TradeStatusEnum.values()){
            if(tradeStatusEnum.code.equals(code)){
                return tradeStatusEnum.desc;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
