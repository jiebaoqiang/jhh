package com.jhh.dc.baika.dao;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.entity.loan.CollectorsList;


public interface CollectorsRecordMapper extends Mapper<CollectorsList> {


    /**
     * 查询催收人今天新增期数
     * @param map
     * @return
     */
    int selectAddPeriodsNumToday(Map map);
    /**
     * 查询催收人昨天新增期数
     * @param map
     * @return
     */
    int selectAddPeriodsNumYesterday(Map map);

}
