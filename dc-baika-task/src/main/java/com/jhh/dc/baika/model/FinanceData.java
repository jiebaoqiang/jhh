package com.jhh.dc.baika.model;


import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

@ExcelTarget("FinanceData")
@Getter
@Setter
public class FinanceData {

    @Excel(name = "订单编号", width = 30, orderNum = "1")
    private String borrNum;


    //还款流水号
    @Excel(name = "支付流水号", width = 40, orderNum = "2")
    private String serialNo;
    //sid
    private String sidNo;
    //type
    private String typeNo;

    @Excel(name = "发生时间", width = 20, orderNum = "3")
    private String repaymentDate;

    @Excel(name = "收入金额", width = 20, orderNum = "4")
    private String money;

    @Excel(name = "还款类型", width = 20, orderNum = "5")
    private String type;

    private String typeWithChannel;

}
