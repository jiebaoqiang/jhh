package com.jhh.dc.baika.api.notice;

import com.jhh.dc.baika.entity.app.NoteResult;

/**
 * Created by wanzezhong on 2018年5月29日 14:41:39
 * 用户行为通知服务
 * @author carl.wan
 */
public interface UserBehaviorNoticeService {

    /**
     * 注册通知
     * @return
     */
    NoteResult registerNotice();

    /**
     * 登录通知
     * @return
     */
    NoteResult loginNotice();

    /**
     * 添加注册缓存
     * @param phone
     */
    void addRegisterRedis(String phone);
    /**
     * 添加登录缓存
     * @param phone
     */
    void addLoginRedis(String phone);

    /**
     * 删除注册缓存
     * @param phone
     */
    void delRegisterRedis(String... phone);

    /**
     * 删除登录缓存
     * @param phone
     */
    void delLoginRedis(String... phone);
}
