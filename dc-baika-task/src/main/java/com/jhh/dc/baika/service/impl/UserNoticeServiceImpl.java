package com.jhh.dc.baika.service.impl;

import com.jhh.dc.baika.service.UserNoticeService;
import com.jhh.dc.baika.api.notice.UserBehaviorNoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wanzezhong on 2018/5/30.
 */
@Service
public class UserNoticeServiceImpl implements UserNoticeService {

    @Autowired
    private UserBehaviorNoticeService userBehaviorNoticeService;

    @Override
    public void registerNotice() {
        userBehaviorNoticeService.registerNotice();
    }

    @Override
    public void loginNotice() {
        userBehaviorNoticeService.loginNotice();
    }
}
