package com.jhh.dc.baika.mapper.baikelu;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

import com.jhh.dc.baika.entity.baikelu.BaikeluRemind;

public interface BaikeluRemindMapper extends Mapper<BaikeluRemind> {
    void insertBaikeluRemindList(List<BaikeluRemind> list);
    void updateBaikeluRemindList(List<BaikeluRemind> list);
}