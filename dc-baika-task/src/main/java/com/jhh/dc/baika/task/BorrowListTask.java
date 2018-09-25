package com.jhh.dc.baika.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jhh.dc.baika.service.BorrListService;


/**
 * Created by wanzezhong on 2018/1/25.
 */
@Component
public class BorrowListTask {

    private Logger logger = LoggerFactory.getLogger(BorrowListTask.class);

    @Autowired
    private BorrListService borrListService;

    /**
     * 拒绝人工审核
     */
    @Scheduled(cron = "0 0 20 * * ? ") // 晚上八点开始
    public void rejectAudit() {
        logger.info("rejectAudit start");
        borrListService.rejectAudit();
    }

    /**
     * 特殊分单
     * 由凌晨五点改为中午十二点分单
     */
    @Scheduled(cron = "0 0 5 * * ? ")
    public void submenuTransfer() {
        logger.info("submenuTransfer start");
        borrListService.submenuTransfer();
    }


    /**
     * 逾期三天分给杨艳（四期产品）
     */
//    @Scheduled(cron = "0 0 05 * * ? ")
    public void overdueThree(){
        logger.info("overdueThree start");
        borrListService.updateOverdueThree();
    }

    /**
     * 逾期两天分给杨艳（一期产品）
     */
//    @Scheduled(cron = "0 0 05 * * ? ")
    public void overdueTwo(){
        logger.info("overdueThree start");
        borrListService.updateOverdueTwo();
    }


    /**
     * 批量代扣
     */
    //@Scheduled(cron = "0 0 23 * * ? ") // 晚上11点开始
    public void batchWithhold() {
        logger.info("batchWithhold start");
        borrListService.batchWithhold();
    }


    /**
     * 查询逾期未还的合同的一天内的最近一笔订单状态和订单失败的原因,早上6点运行定时任务。并标记下来：
     */
    @Scheduled(cron = "0 0 06 * * ? ") // 早上6点开始
    public void batchQueryResult() {
        logger.info("batchQueryResult start");
        borrListService.batchQueryResult();
    }

}
