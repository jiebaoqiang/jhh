package com.jhh.dc.baika.manage.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.entity.manager.CollectorsRemark;

public interface CollectorsRemarkMapper extends Mapper<CollectorsRemark> {

    List<CollectorsRemark> selectRemarkInfo(Map<String, Object> paramMap);
}
