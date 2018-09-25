package com.jhh.dc.baika.manage.pojo.auth;

import java.util.List;

/**
 * Created by chenchao on 2017/10/13.
 */
public interface Module {
    String getId();

    String getDesc();

    String getIndex();

    String getType();

    List<Role> getRoles();
}
