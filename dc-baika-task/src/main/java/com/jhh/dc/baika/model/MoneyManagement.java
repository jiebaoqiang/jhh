package com.jhh.dc.baika.model;

import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;

/**
 * Created by wanzezhong on 2017/9/22.
 */
@ExcelTarget("moneyManagement") @Getter @Setter
public class MoneyManagement implements Serializable{
    @Excel(name="ID", orderNum="1")
    private Integer id ;
    @Excel(name="出借编号", orderNum="2")
    private String borrNum;
    @Excel(name="放款日期", orderNum="3")
    private String payDate;
    @Excel(name="客户姓名", orderNum="4")
    private String name;
    @Excel(name="客户身份证号", orderNum="5")
    private String cardNum;
    @Excel(name="合同金额", orderNum="6")
    private String borrAmount;
    @Excel(name="放款金额", orderNum="7")
    private String payAmount;
    @Excel(name="本金", orderNum="8")
    private String monthMoney;
    @Excel(name="押金", orderNum="9")
    private String depositAmount;

    @Excel(name="租金", orderNum="10")
    private String rentalAmount;
    @Excel(name="赎回金额", orderNum="11")
    private String ransomAmount;
    @Excel(name="贷款期限(天）", orderNum="12")
    private String deadline;
    @Excel(name="签订日", orderNum="13")
    private String makeborrDate;
    @Excel(name="交易日", orderNum="14")
    private String dealDay;
    @Excel(name="计息日", orderNum="15")
    private String datedDate;
    @Excel(name="到期日", orderNum="16")
    private String planrepayDate;
    @Excel(name="开户行", orderNum="17")
    private String bankName;
    @Excel(name="开户名", orderNum="18")
    private String accountName;
    @Excel(name="开户卡号", orderNum="19")
    private String bankNum;
    @Excel(name="手机号码", orderNum="20")
    private String phone;
    @Excel(name="产品名称", orderNum="21")
    private String productName;
    @Excel(name="收回金额", orderNum="22")
    private String recoverableAmount;
    @Excel(name="优惠金额", orderNum="23")
    private String couponAmount;
    @Excel(name="支付流水", orderNum="24")
    private String serialNo;
    @Excel(name="放款渠道", orderNum="24")
    private String type;
    @Excel(name="员工编号", orderNum="25")
    private String employNum;
    @Excel(name="员工姓名", orderNum="26")
    private String employeeName;
    @Excel(name="渠道", orderNum="27")
    private String channel;

    private String typeWithChannel;
}
