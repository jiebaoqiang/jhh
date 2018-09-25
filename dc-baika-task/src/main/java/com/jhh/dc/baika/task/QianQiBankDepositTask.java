package com.jhh.dc.baika.task;

import com.jhh.dc.baika.api.bankdeposit.QianQiDepositBackService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.depository.DepositoryCallbackService;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.dao.BorrowListMapper;
import com.jhh.dc.baika.dao.LoanOrderDOMapper;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.model.LoanOrderDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @author  luolong
 * @date 2018-1-29
 */
@Component
@Slf4j
public class QianQiBankDepositTask {

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private QianQiDepositBackService qianQiDepositBackService;

    @Autowired
    private DepositoryCallbackService depositoryCallbackService;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void task(){
        qianQiDepositBackService.queryAccountAuthAll();
    }

    /**
     *  存管充值主动查询
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void depositoryRecharge(){
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectProcessOrderByType(Constants.payOrderType.DEPOSITORY_RECHARGE_TYPE);
        if (loanOrderDOList !=null && loanOrderDOList.size()> 0){
            loanOrderDOList.forEach(v ->  depositoryCallbackService.searchRechargeOrder(v.getSerialNo()));

        }
    }

    /**
     *  存管还款主动查询
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void depositoryRepay(){
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectProcessOrderByType(Constants.payOrderType.DEPOSITORY_REPAY_TYPE);
        if (loanOrderDOList !=null && loanOrderDOList.size()> 0){
            loanOrderDOList.forEach(v ->  depositoryCallbackService.searchRepayOrder(v.getSerialNo()));

        }
    }

    /**
     *  存管提现主动查询
     */
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void depositoryWithdrawal(){
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectOrderByType(Constants.payOrderType.DEPOSITORY_WITHDRAWAL_TYPE);
        if (loanOrderDOList !=null && loanOrderDOList.size()> 0){
            loanOrderDOList.forEach(v ->  depositoryCallbackService.searchWithdrawalOrder(v.getSerialNo()));

        }
    }

    /**
     *  逾期合同推送幼兔
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void depositoryOverdue(){
        log.info("查询逾期合同开始--------------------------------------------");
        depositoryCallbackService.depositoryOverdue();
    }


}
