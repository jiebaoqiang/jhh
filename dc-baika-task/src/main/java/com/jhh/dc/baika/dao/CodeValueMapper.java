package com.jhh.dc.baika.dao;


import org.apache.ibatis.annotations.Param;

import com.jhh.dc.baika.entity.manager.CodeValue;

import java.util.List;

public interface CodeValueMapper {

    List<CodeValue> getCodeValueListByCode(String code_type);
}