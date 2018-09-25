package com.jhh.dc.baika.model;


import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

@ExcelTarget("RobotData")
public class PromptData {
    @Excel(name = "订单编号", width = 30, orderNum = "1")
    private String borrNum;
    @Excel(name = "手机号码", width = 20, orderNum = "2")
    private String phone;
    @Excel(name = "租金", width = 20, orderNum = "3")
    private Integer money;
    @Excel(name = "违约几天", width = 20, orderNum = "4")
    private final String planRepayDate = "1";
    @Excel(name = "违约金", width = 20, orderNum = "5")
    private String penalty;
    @Excel(name = "共缴金额", width = 20, orderNum = "6")
    private Integer total;

    public String getBorrNum() {
        return borrNum;
    }

    public void setBorrNum(String borrNum) {
        this.borrNum = borrNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public String getPlanRepayDate() {
        return planRepayDate;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
