package com.jhh.dc.baika.manage.entity;

import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

@Getter
@Setter
public class LoansRemarkVo implements Serializable {
    @Excel(name="逾期天数")
    private String bedueDays;
    @Excel(name="姓名")
    private String customerName;
    private String customerId;
    @Excel(name="身份证",width = 25)
    private String customerIdValue;
    @Excel(name="手机号码",width = 15)
    private String customerMobile;
    private String productId;
    @Excel(name="产品类型")
    private String productName;
    @Excel(name="贷款金额")
    private String amount;
    @Excel(name="逾期应还")
    private String bedueAmount;
    @Excel(name="剩余还款")
    private String restAmount;
    @Excel(name="到期日")
    private String endDateString;
    @Excel(name="借款状态")
    private String stateString;
    @Excel(name="催收人")
    private String auditer;
    @Excel(name="最新催收时间",width = 25)
    private String lastCallDateString;
    @Excel(name="合同编号",width = 20)
    private String contractNum;
    private String contractKey;
    @Excel(name="放款金额")
    private String loanAmount;
    private String repayAmount;
    @Excel(name = "催款备注",width = 30)
    private String remark;
    @Excel(name = "添加备注时间",width = 25)
    private String lastCall;
    @Excel(name = "添加备注人")
    private String callName;
}
