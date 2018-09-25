package com.jhh.dc.baika.entity.app_vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class BorrowVO implements Serializable {
    private static final long serialVersionUID = 5997179661476114945L;
    /**
     * 借款单Id
     */
    private Integer borrId;
    /**
     * 用户Id
     */
    private Integer perId;
    /**
     * 用户名
     */
    private String personName;
    /**
     * 逾期天数
     */
    private Integer overdueDays;
    /**
     * 身份证号码
     */
    private String cardNum;
    /**
     * 手机号码
     */
    private String phone;

}
