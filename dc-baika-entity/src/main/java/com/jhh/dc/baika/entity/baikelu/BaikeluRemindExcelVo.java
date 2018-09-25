package com.jhh.dc.baika.entity.baikelu;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;

@ExcelTarget("BaikeluRemindData")
public class BaikeluRemindExcelVo implements Serializable{

    @Excel(name = "订单编号", width = 30, orderNum = "1")
    private String job_ref;
    @Excel(name = "电话号码", width = 20, orderNum = "2")
    private String phone_num;
    @Excel(name = "标记", width = 20, orderNum = "5")
    private String remind_type;
//    @Excel(name = "姓名", width = 20, orderNum = "3")
//    private String user_name="/";
//    @Excel(name = "性别", width = 20, orderNum = "4")
//    private String sex="/";
//    @Excel(name = "状态", width = 20, orderNum = "6")
//    private String status;

    public String getJob_ref() {
        return job_ref;
    }

    public void setJob_ref(String job_ref) {
        this.job_ref = job_ref;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getRemind_type() {
        return remind_type;
    }

    public void setRemind_type(String remind_type) {
        this.remind_type = remind_type;
    }
}
