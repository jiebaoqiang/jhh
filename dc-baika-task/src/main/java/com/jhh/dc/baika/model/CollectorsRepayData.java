package com.jhh.dc.baika.model;

import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

@ExcelTarget("CollectorsRepayData")
@Getter
@Setter

public class CollectorsRepayData {

    @Excel(name = "催收员", width = 30, orderNum = "1")
    private String collectorName;

    @Excel(name = "分配期数（三到七天）", width = 30, orderNum = "2")
    private String periodsNum;

    @Excel(name = "完成期数（三到七天）", width = 30, orderNum = "3")
    private String completePeriodsNum;

    @Excel(name = "还款金额（八天及以上）", width = 30, orderNum = "4")
    private String repaymentAmount;
}
