package com.jhh.dc.baika.entity.share;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//用户佣金领取审核表
@Entity @Table(name = "commission_review") @Getter @Setter
public class CommissionReview implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  //编号

    private String perId; //用户ID

    private String phone;//用户手机号

    private int isChannel;//是否渠道商 0是  1否

    private String commissionOrderIds;//申领佣金单号集合，逗号分隔

    private BigDecimal applyAmount;//申领佣金金额

    private Date applyDate;//申领时间

    private int status;//0 未审核 1 同意 2 拒绝 3 同意但放款失败

    private String reason;//审核拒绝原因

    private Date reviewDate;//审核时间

    private  String  employNum;//审核人员工编号

    private int  loanOrderId;//佣金放款ID

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CommissionReview{");
        sb.append("id=").append(id);
        sb.append(", perId='").append(perId).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", isChannel=").append(isChannel);
        sb.append(", commissionOrderIds='").append(commissionOrderIds).append('\'');
        sb.append(", applyAmount=").append(applyAmount);
        sb.append(", applyDate=").append(applyDate);
        sb.append(", status=").append(status);
        sb.append(", reason='").append(reason).append('\'');
        sb.append(", reviewDate=").append(reviewDate);
        sb.append(", employNum='").append(employNum).append('\'');
        sb.append(", loanOrderId=").append(loanOrderId);
        sb.append('}');
        return sb.toString();
    }
}