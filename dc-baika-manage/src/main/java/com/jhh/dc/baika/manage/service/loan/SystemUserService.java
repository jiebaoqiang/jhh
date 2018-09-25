package com.jhh.dc.baika.manage.service.loan;


import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.entity.Result;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.manager.LoanCompany;
import com.jhh.dc.baika.entity.utils.ManagerResult;

import java.util.List;
import java.util.Map;

public interface SystemUserService {
    /**
     * 查询系统用户
     * @param queryMap
     * @param offset
     * @param size
     * @param userNo
     * @return
     */
    PageInfo<SystemUser> selectCollectors(Map<String, Object> queryMap, int offset, int size, String userNo);

    /**
     * 生成不重复的工号
     * @return
     */
    String generateNewSysno();

    /**
     * 操作系统用户信息
     * @param collectorsLevel
     * @return
     */
    int editCollectors(SystemUser collectorsLevel);

    /**
     * 获取登录用户
     * @param userName
     * @param password
     * @return
     */
    Result loadLoginUser(String userName, String password, Integer source, String loginVerifyCode);

    /**
     * 编辑公司信息
     * @param company
     * @return
     */
    int editCompanyInfo(LoanCompany company);

    /**
     * 发送验证码
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    Result sendVerifyCode(String userName, String password);

    /**
     * 校验登录名
     * @param loginname
     * @return
     */
    Result checkLoginName(String loginname);

    /**
     * 是否为外包
     * @param userSysNo
     * @return
     */
    Boolean isOutWorker(String userSysNo);

    /**
     * 修改密码
     * @param userNo
     * @param oldPwd
     * @param newPwd
     * @param newPwdConfirm
     * @return
     */
    Result modifyPassword(String userNo, String oldPwd, String newPwd, String newPwdConfirm);

    /**
     * 查询风控人员
     * @return
     */
    List<SystemUser> selectCollectors();

    /**
     * 获取权限信息
     * @param userAuth
     * @return
     */
    Result loadUserAuthInfo(String userAuth);

    /**
     * 是否为可用的管理员
     * @param userName
     * @return
     */
    boolean isManager(String userName);

    /**
     * 加载人员权限信息
     * @return
     * @param category
     */
    Result loadUserRoleInfo(String category);

    /**
     * 获取用户权限信息
     * @param userName
     * @return
     */
    String getUserAuth(String userName);
    /**
     * 根据用户名查询渠道商的source
     */
    String getChannelSource(String userName);

    /**
     * 获取渠道商扣量百分比列表
     */
    List<SystemUser> queryChannelCollectors();

    /**
     * 更新渠道商扣量百分比
     */
    ManagerResult updateChannelPercent(SystemUser collectors);

}
