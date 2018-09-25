package com.jhh.dc.baika.app.common.constant;

import java.io.Serializable;

/**
 * Created by xuepengfei on 2018/1/26.
 */
public class Admin implements Serializable{
    private String code;//200表示成功
    private String info;//消息
    private String data;//返回数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
