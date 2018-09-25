package com.jhh.dc.baika.manage.entity;


import java.io.Serializable;

/**
 * 具有分页功能的数据响应对象
 *
 * @param <T>
 */
public class SimpleResp<T> implements Serializable {
    private static final long serialVersionUID = 6104449560649511602L;
    /**
     * 响应码
     */
    private int code = 200;
    /**
     * 响应消息
     */
    private String msg = "成功";
    /**
     * 总记录数
     */
    private long total;
    /**
     * 记录集
     */
    private T record;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }
}
