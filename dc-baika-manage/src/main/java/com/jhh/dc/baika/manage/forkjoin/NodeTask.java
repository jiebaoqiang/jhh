package com.jhh.dc.baika.manage.forkjoin;

import com.google.common.collect.Lists;
import com.jinhuhang.risk.dto.plan.jsonbean.RiskPerNodeInfoDto;
import com.jinhuhang.risk.service.impl.node.UserServiceClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class NodeTask extends RecursiveTask<List> {
    private UserServiceClient nodeClient=new UserServiceClient();

    private static int CAPACITY = 500;

    /**
     * 定义最大计算容量
     */
    private int capacity;

    /**
     * 身份证ID
     */
    List<String> idNumbers;
    String productId;

    public NodeTask(List<String> idNumbers,String productId) {
        this.idNumbers = idNumbers;
        this.productId=productId;
    }

    @Override
    protected List<RiskPerNodeInfoDto> compute() {
        if (idNumbers.size() <= CAPACITY) {
            List<RiskPerNodeInfoDto> riskPerNodeInfoDtos=null;
            try {
                riskPerNodeInfoDtos = nodeClient.selectRecentPerNodeRecord(idNumbers, productId);
            } catch (Exception e) {
                log.error("获取风控认证节点异常", e);
                return Lists.newArrayList();
            }

            if (riskPerNodeInfoDtos==null||riskPerNodeInfoDtos.size()==0) {
                return Lists.newArrayList();
            }

            return riskPerNodeInfoDtos;
        }

        int middle = idNumbers.size() / 2;
        List<String> l1 = Lists.newArrayList();
        List<String> l2 = Lists.newArrayList();
        for (int i = 0; i < idNumbers.size(); i++) {
            if (i < middle) {
                l1.add(idNumbers.get(i));
            } else {
                l2.add(idNumbers.get(i));
            }
        }

        NodeTask task1 = new NodeTask(l1,productId);
        NodeTask task2 = new NodeTask(l2,productId);
        invokeAll(task1, task2);

        List result = Lists.newArrayList();
        result.addAll(task1.join());
        result.addAll(task2.join());

        return result;
    }
}
