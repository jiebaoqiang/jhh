package com.jhh.dc.baika.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 2018/7/13.
 */
@Data
public class UserNodeDo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  用户所在节点
     */
    private String node;

    /**
     *  产品类型 区分悠兔白卡 悠兔白卡等产品地址
     */
    private String prodType;

    /**
     * 合同编号
     */
    private String borrNum;

    /**
     * 产品Id
     */
    private Integer prodId;

    /**
     * 跳转路径
     */
    private String forwardUrl;
}
