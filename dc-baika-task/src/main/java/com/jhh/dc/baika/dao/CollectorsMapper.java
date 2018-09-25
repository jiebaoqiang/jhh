package com.jhh.dc.baika.dao;

import org.apache.ibatis.annotations.Param;

import com.jhh.dc.baika.entity.loan.CollectorsList;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface CollectorsMapper extends Mapper<CollectorsList> {


    /**
     * 查询公司催收和外包催收人员的名称和编号
     * @return
     */
    List<Map<String,String>> selectCollectors();

}
