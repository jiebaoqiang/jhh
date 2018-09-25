package com.jhh.dc.baika.entity.app_vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: wenfucheng
 * @date: 2018/7/26 14:34
 * @description: 京东卡账号密码关键信息实体类
 */
@Data
public class JdCardKeyInfo implements Serializable {
    /**
     * 卡号
     */
    private String cardNum;

    /**
     * 卡密
     */
    private String password;

    /**
     * 有效期起始时间
     */
    private String startDate;

    /**
     * 有效期结束时间
     */
    private String endDate;
}
