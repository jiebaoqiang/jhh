package com.jhh.dc.baika.task;

import com.jhh.dc.baika.api.app.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 2018/8/1.
 * 同步合同最终状态给A
 */
@Component
@Slf4j
public class SynchBorrowStatusTask {

    @Autowired
    private LoanService loanService;

    @Scheduled(cron = "0 0/3 * * * ?")
    public void synchBorrowStatus(){
       log.info("同步查询borrowStatus给A公司开始同步最终状态-----------------");
       loanService.synchBorrowStatus();
    }

    @Scheduled(cron = "0 0 5 * * ?")
    public void synchBorrowStatusOverdue(){
        log.info("同步查询borrowStatus给A公司开始同步逾期状态-----------------");
        loanService.synchBorrowStatusOverdue();
    }
}
