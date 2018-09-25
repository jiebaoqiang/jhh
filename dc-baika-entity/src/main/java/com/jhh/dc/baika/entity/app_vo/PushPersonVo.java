package com.jhh.dc.baika.entity.app_vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: wenfucheng
 * @date: 2018/9/12 12:10
 * @description:
 */
@Data
public class PushPersonVo implements Serializable {

    /**
     *  手机号
     */
    private String phone;

    /**
     * 身份类型
     */
    private int identityType;

    /**
     * 来源:1.web,2.iOS,3.android
     */
    private int source;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String card;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String sex;

}
