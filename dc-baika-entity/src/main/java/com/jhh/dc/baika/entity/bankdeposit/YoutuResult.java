package com.jhh.dc.baika.entity.bankdeposit;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangqi on 2018/9/10.
 */
@Setter
@Getter
public class YoutuResult<T> {

    private T data;
    private String message;
    private String ret;

}
