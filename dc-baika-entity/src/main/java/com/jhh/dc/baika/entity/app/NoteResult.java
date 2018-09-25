package com.jhh.dc.baika.entity.app;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * 结果对象  初始化默认201 失败
 * @author xuepengfei
 * 2016年9月28日上午9:22:37
 * {"status":"200","info":"成功","data":xxx}
 */
@Getter
@Setter
@ToString
public class NoteResult implements Serializable {


    private String code;//200表示成功,其他表示失败
    private String info;//消息
    private Object data;//返回的数据

    public NoteResult(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public NoteResult() {

    }

    public static NoteResult FAIL_RESPONSE(String msg) {
        NoteResult res = new NoteResult();
        res.setCode("201");
        res.setInfo(msg);
        return res;
    }


    public static NoteResult SUCCESS_RESPONSE() {
        return SUCCESS_RESPONSE("成功");
    }

    public static NoteResult SUCCESS_RESPONSE(String msg) {
        NoteResult res = new NoteResult();
        res.setCode("200");
        res.setInfo(msg);
        return res;
    }
}

