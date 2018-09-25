package com.jhh.dc.baika.entity.app_vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther: wenfucheng
 * @date: 2018/7/19 16:35
 * @description:
 */
@Data
public class JdCardDetailVO extends JdCardInfoVO implements Serializable {

    /**
     * 用户id
     */
    private Integer perId;

    /**
     * 状态名称
     */
    private String borrStatusName;

    /**
     * 状态
     */
    private String borrStatus;
    /**
     * 产品类型
     */
    private String prodType;
    /**
     *  合同编号
     */
    private String borrNum;
    /**
     * 产品Id
     */
    private Integer prodId;



}
