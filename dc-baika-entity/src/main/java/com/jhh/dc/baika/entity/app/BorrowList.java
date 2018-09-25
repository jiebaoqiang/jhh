package com.jhh.dc.baika.entity.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jhh.dc.baika.common.util.BorrNum_util;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.id.annotation.IdGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "b_borrow_list")
@Getter
@Setter
@ToString
public class BorrowList implements Serializable {

    public static final int SEND_LOAN_MESSAGE = 1 << 2;

    private static final long serialVersionUID = 2510888469306589931L;
    @IdGenerator(value = "dc_baika_borrow_list")
    @Id
    private Integer id;

    private Integer perId;

    private Integer prodId;
    @JsonFormat(pattern=" yyyy-MM-dd HH:mm:ss")
    private Date askborrDate;

    private String borrNum;
    @JsonFormat(pattern=" yyyy-MM-dd HH:mm:ss")
    private Date makeborrDate;
    @JsonFormat(pattern=" yyyy-MM-dd HH:mm:ss")
    private Date payDate;
    @JsonFormat(pattern=" yyyy-MM-dd HH:mm:ss")
    private Date planrepayDate;
    @JsonFormat(pattern=" yyyy-MM-dd HH:mm:ss")
    private Date actRepayDate;

    private String borrStatus;

    private Integer overdueDays;
    @Transient
    private Float surplusPenalty;
    @Transient
    private Float planRental;

    private Float planRepay;
    @Transient
    private Float actPlanAmount;
    @Transient
    private Float actRepayAmount;

    private Float actReduceAmount;
    @Transient
    private Float surplusAmount;

    /**
     *  对应数据库字段与surplusAmount其实是同一个不想改了。。。
     */
    private Float amountSurplus;

    private Integer ispay;

    private Integer termNum;

    private Integer totalTermNum;
    
    private Float borrAmount;

    @Transient
    private Integer noDepositRefund;
    @Transient
    private Float depositAmount;

    private Float payAmount;
    @Transient
    private Float ransomAmount;
    @Transient
    private Float surplusRansomAmount;
    @Transient
    private Float surplusRentalAmount;
    @Transient
    private Integer perCouponId;

    private Date currentRepayTime;

    private String collectionUser;
    @JsonFormat(pattern=" yyyy-MM-dd HH:mm:ss")
    private Date currentCollectionTime;
    @JsonFormat(pattern=" yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    private Integer updateUser;
    @JsonFormat(pattern=" yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    private Integer creationUser;

    private Integer version;

    private Integer baikeluStatus;

    private String borrUpStatus;

    private String description;

    private Integer contactNum;

    private Integer isManual;

    private Integer flag;

    private Float serviceAmount;

    private String loanUse;

   
    //private Integer termday;
    private Float amountRepay;

    private String prodType;

    @Transient
    private Float penalty;



    private Integer noServiceRefund;

    private Integer isTop;

    private Date firstOverdueDate;

    private BigDecimal capitalSum;

    private BigDecimal capitalRepay;

    private BigDecimal capitalSurplus;

    private BigDecimal interestRate;

    private BigDecimal interestSum;

    private BigDecimal interestRepay;

    private BigDecimal interestSurplus;

    private BigDecimal penaltyAmount;

    private BigDecimal penaltySum;

    private BigDecimal penaltyRepay;

    private BigDecimal penaltySurplus;

    private BigDecimal forfeitRate;

    private BigDecimal forfeitSum;

    private BigDecimal forfeitRepay;

    private BigDecimal forfeitSurplus;

    private BigDecimal amountSum;

    private BigDecimal balance;

    private Integer repayRetryFlag;

    private Integer sync;
    @Transient
    private String borrStatusName;

    private String investBorrowId;

    /**
     *  账户管理费
     */
    private BigDecimal accountManageSurplus;

    /**
     *  信息服务费
     */
    private BigDecimal informationServiceSurplus;

    public BorrowList(){

    }

    /**
     * 申请借款订单生产
     */
    public BorrowList(Product product,Person person,String borrUpStatus){
        this.perId = person.getId();
        this.prodId = product.getId();
        this.askborrDate = new Date();
        this.borrNum = "dc" + BorrNum_util.getStringRandom(12);
        this.borrStatus = CodeReturn.STATUS_APLLY;
        this.borrAmount = product.getAmount();
        this.termNum = product.getTermNum();
        //this.termday = product.getTermday();
        this.description = person.getDescription();
        this.contactNum = person.getContactNum();
        this.prodType = product.getProductTypeCode();
        if (StringUtils.isNotBlank(borrUpStatus)){
            this.borrUpStatus = borrUpStatus;
        }
        this.payAmount = product.getAmount();

    }
}