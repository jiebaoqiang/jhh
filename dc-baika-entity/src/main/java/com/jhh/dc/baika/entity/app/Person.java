package com.jhh.dc.baika.entity.app;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="b_person")
@Getter
@Setter
@ToString
@ApiModel("person")
public class Person implements Serializable {
    private static final long serialVersionUID = 734164289531433453L;
    @Id
    private Integer id;

    private String phone;

    private Date createDate;

    private Date updateDate;

    private String sync;

    private String isManual;

    private String description;

    private String cardNum;

    private String name;

    private String bankName;

    private String bankCard;

    private Integer contactNum;

    @Transient
    private BigDecimal balance;

    private Date loginTime;

    private Integer isLogin;

    private String tokenId;

    @Transient
    private String source;
    @Transient
    private String contactUrl;
    @Transient
    private String device;

    private String payPassword;

    private String applyNode;

    private String acctNo;//'前旗银行账户号'

    private String custNo;//前旗银行用户号',

    private String isOpenAccount;//'开户状态:   f:开户失败，s:开户成功，p:开户处理中',

    private String isRepayAuth;//'前旗银行还款转账授权状态: f:失败 s:成功  p:处理中',

    private String isLoanAuth;//'前旗银行商户放款授权状态: f:失败 s:成功  p:处理中',

}