package com.jhh.dc.baika.mapper.app;

import com.jhh.dc.baika.entity.app.PersonInfo;

public interface PersonInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonInfo record);

    int insertSelective(PersonInfo record);

    PersonInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonInfo record);

    int updateByPrimaryKey(PersonInfo record);

    PersonInfo selectByPersonId(Integer perId);
}