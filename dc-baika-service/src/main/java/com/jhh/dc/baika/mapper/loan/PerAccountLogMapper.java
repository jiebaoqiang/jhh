package com.jhh.dc.baika.mapper.loan;

import java.util.List;

import com.jhh.dc.baika.entity.loan.PerAccountLog;

public interface PerAccountLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PerAccountLog record);

    int insertSelective(PerAccountLog record);

    PerAccountLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PerAccountLog record);

    int updateByPrimaryKey(PerAccountLog record);
    
    List<PerAccountLog> getPerAccountLog(String userId, int start, int pageSize);
}