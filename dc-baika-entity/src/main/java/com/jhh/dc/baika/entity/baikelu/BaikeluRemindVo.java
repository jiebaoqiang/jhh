package com.jhh.dc.baika.entity.baikelu;

import java.io.Serializable;

public class BaikeluRemindVo  implements Serializable {

    //请求订单号
    private String csv_arn;
    //请求手机号
    private String csv_phone_num;
    //提醒类型
    private String csv_tag;

    public String getCsv_arn() {
        return csv_arn;
    }

    public void setCsv_arn(String csv_arn) {
        this.csv_arn = csv_arn;
    }

    public String getCsv_phone_num() {
        return csv_phone_num;
    }

    public void setCsv_phone_num(String csv_phone_num) {
        this.csv_phone_num = csv_phone_num;
    }

    public String getCsv_tag() {
        return csv_tag;
    }

    public void setCsv_tag(String csv_tag) {
        this.csv_tag = csv_tag;
    }
}
