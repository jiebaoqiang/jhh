package com.jhh.dc.baika.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jhh.dc.baika.service.BorrListService;
import com.jhh.dc.baika.service.RobotService;

@Component
public class RobotTask {

    @Autowired
    private RobotService robotService;
    @Autowired
    private BorrListService borrListService;

    private Logger logger = LoggerFactory.getLogger(RobotTask.class);
    /**
     * 给百可录发数据,逾期一天提醒
     */
    //@Scheduled(cron = "0 0 08 * * ? ")
    public void sendDataToBaikelu() {
        robotService.sendDataToBaikelu();
    }

    /**
     * 机器人上午打电话
     * 九点给昨天签约的人打首单审核电话
     */
    //@Scheduled(cron = "0 0 09 * * ? ")
    public void rcCallPhone() {
        logger.info("首单审核定时任务 rcCallPhone start");
        borrListService.rcCallPhone();
    }
}
