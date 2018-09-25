package com.jhh.dc.baika.entity;

import com.jhh.dc.baika.constant.HaierConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 2017/12/18.
 */
@Setter
@Getter
@ToString
public class HaierBaseVo implements Serializable{

    private static final long serialVersionUID = 6141368272780545054L;

    /**
     * 请求号，字母数字下划线，确保每次请求唯一
     */
    public String request_no;
    /**
     *  接口名称
     */
    private String service;
    /**
     *  接口版本
     */
    private double version = HaierConstants.VERSION;
    /**
     *  平台商户号
     */
    private String partner_id = HaierConstants.PARTNER_ID;
    /**
     *  参数编码字符集
     */
    private String charset = HaierConstants.CHARSET;
    /**
     *  签名
     */
    private String sign;
    /**
     *  签名方式
     */
    private String sign_type = HaierConstants.SIGN_TYPE;
    /**
     *  请求时间，格式yyyy-MM-dd HH:mm:ss
     */
    private String timestamp;

    /**
     * 务请求参数集合支持的格式，仅支持 JSON
     */
    private String format = "JSON";
    /**
     *  请求参数集合
     */
    private String biz_content;

}
