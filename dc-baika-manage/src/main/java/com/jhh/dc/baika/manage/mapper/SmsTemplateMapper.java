package com.jhh.dc.baika.manage.mapper;


import java.util.List;

import com.jhh.dc.baika.entity.manager.SmsTemplate;

public interface SmsTemplateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SmsTemplate record);

    SmsTemplate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsTemplate record);

    int updateByPrimaryKey(SmsTemplate record);
    
    List<SmsTemplate> getAllSmsTemplateList();
}