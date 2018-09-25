package com.jhh.dc.baika.manage.mapper;

import java.util.Map;

import com.jhh.dc.baika.entity.app.Card;

public interface CardMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Card record);

    int insertSelective(Card record);

    Card selectByPrimaryKey(Integer id);
    
    //根据per_id查询身份证
    Card selectByPerId(Integer per_id);
    
    //根据身份证号查询Card
    Card selectByCardNo(String card_num);
 
    int updateByPrimaryKeySelective(Card record);

    int updateByPrimaryKey(Card record);
    //查询百可录首单问题信息
    Map queryRobot(Integer borrId);
    
    
}