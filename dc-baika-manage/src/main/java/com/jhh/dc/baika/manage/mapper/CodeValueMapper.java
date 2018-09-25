package com.jhh.dc.baika.manage.mapper;


import org.apache.ibatis.annotations.Param;

import com.jhh.dc.baika.entity.manager.CodeValue;

import java.util.List;

public interface CodeValueMapper {
	
	List<CodeValue> getCodeValueListByCode(String code_type);
	
    int deleteByPrimaryKey(Integer id);

    int insert(CodeValue record);

    int insertSelective(CodeValue record);

    CodeValue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CodeValue record);

    int updateByPrimaryKey(CodeValue record);
    
    //根据type和code查询meaning
    String getMeaningByTypeCode(@Param("codeType") String code_type,@Param("codeCode") String code_code);

    /**
     *  根据 description 查询数据来源
     * @param description 描述
     * @return
     */
    List<String> getSourceByDesc(@Param("description") String description);

    String getCodeByType(String code_type);

    List<CodeValue> getEnableCodeValueListByCodeType(String code_type);

    CodeValue selectByCodeType(@Param("codeType") String codeType);
}