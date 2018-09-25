package com.jhh.dc.baika.mapper.manager;

import org.apache.ibatis.annotations.Param;

import com.jhh.dc.baika.entity.manager.Msg;

import java.util.List;

public interface MsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Msg record);

    int insertSelective(Msg record);

    Msg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Msg record);

    int updateByPrimaryKeyWithBLOBs(Msg record);

    int updateByPrimaryKey(Msg record);
    
    List<Msg> getMessageByUserId(@Param("perId") String id, @Param("start") int start, @Param("pageSize") int pageSize);
    
    int updateMessageStatus(String msgId);
    
    int selectUnread(String perId);
}