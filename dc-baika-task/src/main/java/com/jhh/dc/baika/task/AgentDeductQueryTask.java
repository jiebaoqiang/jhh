package com.jhh.dc.baika.task;

import com.jhh.dc.baika.api.channel.AgentBatchStateService;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.dao.LoanOrderDOMapper;
import com.jhh.dc.baika.model.LoanOrderDO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 *
 * @author  luolong
 * @date 2018-1-29
 */
@Component
@Slf4j
@SuppressWarnings("SpringJavaAutowiringInspection")
public class AgentDeductQueryTask {

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private AgentChannelService agentChannelService;

    @Autowired
    private AgentBatchStateService batchState;

    @Value("${dbcp.url}")
    private String url;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void doAgentpayQuery(){
        log.info("\n代扣主动查询进来了"+url);
        doIt();
        log.info("\n代扣主动查询结束了");
    }

    private void  doIt(){
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectOrderByDeduct();
        log.info("\n获取需要查询状态的代扣单子开始:在处理的代扣单子数量：" + (loanOrderDOList == null ? 0 : loanOrderDOList.size()));
        try{
            if (loanOrderDOList != null) {
                for (LoanOrderDO loanOrderDO : loanOrderDOList) {
                    log.info("\n当前正在处理的代扣单子：{}",loanOrderDO.getSerialNo());
                    if (agentChannelService == null) {
                        log.info("agentDeductService 为空");
                    }else {
                        log.info("agentDeductService 不为空");
                    }
                    ResponseDo resut = agentChannelService.state(loanOrderDO.getSerialNo());
                    log.info("单子 返回result {}",resut);
                }
            }
        }catch (Exception e){
            log.error("查询代扣订单状态定时器出错",e);
        }
        log.info("\n获取需要查询状态的代扣单子结束");
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void batchDeduct() throws Exception {
        log.info("\n代扣主动查询进来了"+url);
        List<LoanOrderDO> loanOrderDOs = loanOrderDOMapper.selectOrderByBatchDeduct();
        List<String> list = loanOrderDOs.stream().map(LoanOrderDO::getSerialNo).collect(Collectors.toList());
        if (list.size()>0) {
            batchState.batchState(list);
        }
        log.info("\n代扣主动查询结束了");
    }

}
