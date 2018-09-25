package com.jhh.dc.baika.service.impl;

import com.jhh.dc.baika.api.black.RiskBlackService;
import com.jhh.dc.baika.dao.BorrowListMapper;
import com.jhh.dc.baika.entity.app_vo.BorrowVO;
import com.jhh.dc.baika.service.BlackService;
import com.jhh.dc.baika.common.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BlackServiceImpl implements BlackService {

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private RiskBlackService riskBlackService;

    @Override
    public int blackOverdays() {
        System.out.println("执行时间=========================" + DateUtil.getDateString(new Date(), "yyyy-MM-dd hh:mm:ss"));
        log.info("【逾期11天用户拉黑】定时任务开始。。。。");
        List<BorrowVO> voList = borrowListMapper.selectOverDays();
        int size = 0;
        if (!CollectionUtils.isEmpty(voList)) {
            size = voList.size();
            voList.forEach(vo -> {
                riskBlackService.black(vo.getPhone(), vo.getCardNum(), vo.getPersonName(), vo.getPerId());
            });
        }

        log.info("【逾期11天用户拉黑】定时任务结束。。。。");
        return size;
    }
}
