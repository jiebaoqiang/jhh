package com.jhh.dc.baika.manage.mapper;


import java.util.List;

import com.jhh.dc.baika.entity.manager.Msg;

public interface MsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Msg record);

    int insertSelective(Msg record);

    Msg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Msg record);

    int updateByPrimaryKeyWithBLOBs(Msg record);

    int updateByPrimaryKey(Msg record);
    
    List<Msg> getMessageByUserId(String id, int start, int pageSize);
    
    int updateMessageStatus(String msgId);
    
    int selectUnread(String perId);
}