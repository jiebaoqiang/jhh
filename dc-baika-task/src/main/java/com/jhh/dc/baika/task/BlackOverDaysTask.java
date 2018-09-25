package com.jhh.dc.baika.task;

import com.jhh.dc.baika.api.black.RiskBlackService;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jhh.dc.baika.dao.BorrowListMapper;
import com.jhh.dc.baika.entity.app_vo.BorrowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 逾期11天拉黑
 */
@Component
@Slf4j
public class BlackOverDaysTask {

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private RiskBlackService riskBlackService;

    /**
     * 每天每天凌晨4点开始执行此定时
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void black() {
        System.out.println("执行时间========================="+ DateUtil.getDateString(new Date(),"yyyy-MM-dd hh:mm:ss"));
        log.info("【逾期11天用户拉黑】定时任务开始。。。。");
        List<BorrowVO> voList = borrowListMapper.selectOverDays();

        if (!CollectionUtils.isEmpty(voList)) {
            voList.forEach(vo -> {
                riskBlackService.black(vo.getPhone(), vo.getCardNum(), vo.getPersonName(), vo.getPerId());
            });
        }

        log.info("【逾期11天用户拉黑】定时任务结束。。。。");
    }
}
