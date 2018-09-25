package com.jhh.dc.baika.task;

import com.jhh.dc.baika.dao.LoanOrderDOMapper;
import com.jhh.dc.baika.model.LoanOrderDO;
import com.jhh.dc.baika.model.LoanOrderDOExample;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.ResponseDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Copyright © 2018 上海金互行金融服务有限公司. All rights reserved. *
 * @Title:
 * @Prject: dc-loan
 * @Package: com.jhh.dc.baika.task
 * @Description: ${todo}
 * @author: jack liujialin@jinhuhang.com.cn
 * @date: 2018/1/27 15:00
 * @version: V1.0
 */
@Component
@Slf4j
@SuppressWarnings("SpringJavaAutowiringInspection")
public class AgentpayQueryTask {

    @Autowired
    private LoanOrderDOMapper loanOrderDOMapper;

    @Autowired
    private AgentChannelService agentChannelService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void doAgentpayQuery(){
        log.info("\n代付主动查询进来了");
        try {
            doIt();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("定时任务出现异常");
        }
        log.info("\n代付主动查询结束了");

    }

    private void  doIt() throws Exception {
        log.info("\n获取需要查询状态的代付单子开始");

        LoanOrderDOExample loanOrderDOExample = new LoanOrderDOExample();
        LoanOrderDOExample.Criteria cia = loanOrderDOExample.createCriteria();
        List<String> list = new ArrayList<>();
        list.add(Constants.payOrderType.YSB_PAY_TYPE);
        list.add(Constants.payOrderType.HAIER_PAY_TYPE);
        list.add(Constants.payOrderType.PAYCENTER_PAY_TYPE);
        cia.andTypeIn(list);
        cia.andRlStateEqualTo("p");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -3);
        cia.andCreationDateLessThan(beforeTime.getTime());
        List<LoanOrderDO> loanOrderDOList = loanOrderDOMapper.selectByExample(loanOrderDOExample);
        log.info("\n获取需要查询状态的代付单子开始:在处理的代付单子数量：" + (loanOrderDOList == null ? 0 : loanOrderDOList.size()));
        for (LoanOrderDO loanOrderDO : loanOrderDOList) {

            log.info("\n当前正在处理的代付单子：{}",loanOrderDO.getSerialNo());
            if(agentChannelService ==null ){
                log.info("\ndubbo 服务为空");
                return;
            }
            ResponseDo resut = agentChannelService.state(loanOrderDO.getSerialNo());
            log.info("单子 返回result {}",resut);
        }

        log.info("\n获取需要查询状态的代付单子结束");
    }
}
