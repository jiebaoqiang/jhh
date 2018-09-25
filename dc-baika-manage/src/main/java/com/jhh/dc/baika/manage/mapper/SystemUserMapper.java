package com.jhh.dc.baika.manage.mapper;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.entity.loan.SystemUser;

public interface SystemUserMapper extends Mapper<SystemUser> {

    Integer selectMaxId();

    List<SystemUser> selectDsUsers(Map<String, Object> params);

    String getChannelSource(String username);

    String getSysNoByName(String userName);

    String getNameBySysNo(String SysNo);

    List<SystemUser> selectUserBySysNo(String sysNo);

    List<SystemUser> queryChannelCollectors();
}
