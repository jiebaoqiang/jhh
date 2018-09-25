package com.jhh.dc.baika.entity.refund;


import com.jhh.id.annotation.IdGenerator;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

//退款表
@Entity
@Table(name = "b_refund_review") @Getter
@Setter
public class RefundReview {
    @Id
    @IdGenerator(value = "dc_baika_refund_review")
    private Integer id;  //编号

    private Integer perId;//用户id

    private String orderId;//订单号

    private String bankNum;//银行卡号

    private String bankName;//银行名称

    private BigDecimal amount;//金额

    private Integer status;//状态 1:已确认、2:退款中、3:退款失败、4:退款成功、5:退款拒绝

    private String remark;//备注

    private String reviewUser;//审核人

    private Date createDate;//创建时间

    private Date updateDate;//更新时间

    private Date reviewDate;//退款财务审核时间

    private String createUser;//退款申请操作人
}
