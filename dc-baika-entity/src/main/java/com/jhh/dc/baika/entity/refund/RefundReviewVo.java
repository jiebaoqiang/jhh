package com.jhh.dc.baika.entity.refund;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

//退款前台展示List
@Getter
@Setter
public class RefundReviewVo {

    private Integer id;  //退款ID

    private Integer perId;//用户编号

    private String userName;//用户姓名

    private String cardNum;//用户身份证号

    private String phone;//用户手机号

    private BigDecimal balance;//金额

    private String bankName;//银行名称

    private String bankNum;//银行卡号

    private String statusName;//状态描述

    private Integer status;//状态Code 1:已确认、2:退款中、3:退款失败、4:退款成功、5:退款拒绝

    private Date createDate;//创建时间
}
