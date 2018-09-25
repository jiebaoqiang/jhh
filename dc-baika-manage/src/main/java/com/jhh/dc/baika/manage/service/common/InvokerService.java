package com.jhh.dc.baika.manage.service.common;

import com.jhh.dc.baika.entity.app.NoteResult;

public interface InvokerService {

    /**
     * 调用各个组件
     * @return
     */
    NoteResult invokeComponent();
}
