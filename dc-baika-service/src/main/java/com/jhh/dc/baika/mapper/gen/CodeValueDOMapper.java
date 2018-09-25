package com.jhh.dc.baika.mapper.gen;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.jhh.dc.baika.mapper.gen.domain.CodeValueDO;
import com.jhh.dc.baika.mapper.gen.domain.CodeValueDOExample;

public interface CodeValueDOMapper {
    int countByExample(CodeValueDOExample example);

    int deleteByExample(CodeValueDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CodeValueDO record);

    int insertSelective(CodeValueDO record);

    List<CodeValueDO> selectByExample(CodeValueDOExample example);

    CodeValueDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CodeValueDO record, @Param("example") CodeValueDOExample example);

    int updateByExample(@Param("record") CodeValueDO record, @Param("example") CodeValueDOExample example);

    int updateByPrimaryKeySelective(CodeValueDO record);

    int updateByPrimaryKey(CodeValueDO record);
}