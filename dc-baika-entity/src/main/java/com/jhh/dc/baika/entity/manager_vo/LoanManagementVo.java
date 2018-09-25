package com.jhh.dc.baika.entity.manager_vo;

import com.jhh.dc.baika.entity.enums.BorrowStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

//贷后管理VO
@Getter @Setter
public class LoanManagementVo implements Serializable {
    @Excel(name = "逾期天数")
    private Integer bedueDays;
    @Excel(name = "姓名")
    private String customerName;
    private String customerId;
    @Excel(name = "身份证", width = 25)
    private String customerIdValue;
    @Excel(name = "手机号码", width = 15)
    private String customerMobile;
    @Excel(name = "银行名称")
    private String bankName;
    @Excel(name = "银行卡号", width = 20)
    private String bankNum;
    private String productId;
    @Excel(name = "产品类型")
    private String productName;
    @Excel(name = "产品期数")
    private String productTerm;
    @Excel(name = "借款金额")
    private String amount;
    @Excel(name = "应还违约金")
    private String penalty;
    @Excel(name = "应还利息")
    private String interestSum;
    @Excel(name = "应还罚息")
    private String forfeitSum;
    @Excel(name = "应还合计")
    private String totalAmount;
//    @Excel(name = "剩余违约金")
    private String surplusPenalty;
    @Excel(name = "剩余还款总额")
    private String surplusTotalAmount;
    @Excel(name = "到期日")
    private String endDateString;
    @Excel(name = "结清日")
    private String settleDateString;
    @Excel(name = "合同状态")
    private String stateString;

    private String auditer;
    @Excel(name = "最新催收时间")
    private String lastCallDateString;
    @Excel(name = "合同编号", width = 20)
    private String contractID;
    private String contractKey;
    @Excel(name = "放款金额")
    private String loanAmount;

    private String payAmount;
//    @Excel(name = "已减免")
    private String reduceAmount;
    private String createUser;
    @Excel(name = "最新扣款时间")
    private String orderString;
    @Excel(name = "是否黑名单")
    private String blackList;

    private Integer isManual;

    private String description;

    private String borrStatus;
    private String status;
    private String reason;
    private String amountRepay;
    @Excel(name = "催收人")
    private String userName;

    private String termCount;   /*贷款期限*/

    private String shouldPayAmount;//  应还金额  本金+利息
    private String repayRetryFlag;//充值成功还款失败补偿，1 待补偿 2 补偿中 3 已补偿
    public void setBorrStatus(String borrStatus) {
        this.borrStatus = borrStatus;
        this.setStateString(borrStatus);
    }

    public void setStateString(String stateString) {
        String desc = BorrowStatusEnum.getDescByCode(stateString);
        if (StringUtils.isNotBlank(desc)) {
            this.stateString = desc;
        } else {
            this.stateString = stateString;
        }
    }
}