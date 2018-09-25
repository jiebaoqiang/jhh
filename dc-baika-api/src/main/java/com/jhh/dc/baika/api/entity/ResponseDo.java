package com.jhh.dc.baika.api.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 2017/12/27.
 */
@Setter
@Getter
@ToString
public class ResponseDo<T> implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7555308038813543444L;

	private int code;

    private String info;

    private T data;

    public ResponseDo(int code, String info, T data) {
        this.code = code;
        this.info = info;
        this.data = data;
    }

    public ResponseDo(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public ResponseDo() {

    }
    
    public boolean isSuccess() {
    		return this.code == 200;
    }
    
    public static <T> ResponseDo<T> newSuccessDo() {
    		ResponseDo<T> rd = new ResponseDo<>();
    		rd.setCode(200);
    		rd.setInfo("操作成功");
    		return rd;
    }
    public static <T> ResponseDo<T> newSuccessDo(T t) {
    		ResponseDo<T> rd = new ResponseDo<>();
    		rd.setCode(200);
    		rd.setInfo("操作成功");
            rd.setData(t);
    		return rd;
    }

    public static <T> ResponseDo<T> newFailedDo(String msg) {
		ResponseDo<T> rd = new ResponseDo<>();
		rd.setCode(201);
		rd.setInfo(msg);
		return rd;
    }

    public static <T> ResponseDo<T> newFailedDo(int code ,String msg) {
        ResponseDo<T> rd = new ResponseDo<>();
        rd.setCode(code);
        rd.setInfo(msg);
        return rd;
    }

    public static <T> ResponseDo<T> newFailedDo(T t) {
        ResponseDo<T> rd = new ResponseDo<>();
        rd.setCode(201);
        rd.setData(t);
        return rd;
    }
}
