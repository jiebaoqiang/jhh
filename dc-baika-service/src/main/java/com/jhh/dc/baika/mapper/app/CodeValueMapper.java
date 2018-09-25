package com.jhh.dc.baika.mapper.app;

import org.apache.ibatis.annotations.Param;

import com.jhh.dc.baika.entity.manager.CodeValue;

import java.util.List;

/**
 * 2017/12/29.
 */
public interface CodeValueMapper {


    List<CodeValue> getCodeValueAll(@Param("codeType") String codeType);

    //根据type和code查询meaning
    String getMeaningByTypeCode(@Param("code_type") String code_type, @Param("code_code") String code_code);

    List<String> getMeaning(@Param("code_type") String code_type, @Param("code_code") String code_code);

    /**
     * get所有可用的codeValues
     * @param codeType
     * @return codeValues的集合
     */
    List<CodeValue>  getEnabledCodeValues(@Param("codeType") String codeType);

    String selectCodeByType(@Param("codeType") String type);

    CodeValue selectByCodeType(@Param("codeType") String codeType);

    List<String> selectQuickPayChannels();
}
