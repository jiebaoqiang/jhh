package com.jhh.dc.baika.api.common;

import com.jhh.dc.baika.entity.app.NoteResult;

/**
 * 调度链服务
 */
public interface InvokerComponentService {

    /**
     * 调用各个组件
     * @return
     */
    NoteResult invokeComponent();
}
