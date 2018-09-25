package com.jhh.dc.baika.dao;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.entity.loan.CollectorsList;


public interface RepaymentPlanMapper extends Mapper<CollectorsList> {


    /**
     * 查询催收人员当天完成的催收单数
     * @return
     */
    int selectCompletePeriodsNum(Map map);



}
