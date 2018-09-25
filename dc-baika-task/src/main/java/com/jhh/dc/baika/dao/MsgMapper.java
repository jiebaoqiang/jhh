package com.jhh.dc.baika.dao;


import com.jhh.dc.baika.model.Msg;


public interface MsgMapper {

    int insertSelective(Msg record);

}