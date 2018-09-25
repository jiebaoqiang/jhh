package com.jhh.dc.baika.manage.forkjoin;

import com.google.common.collect.Lists;
import com.jinhuhang.risk.dto.QueryResultDto;
import com.jinhuhang.risk.dto.blacklist.jsonbean.BlackListDto;
import com.jinhuhang.risk.service.impl.blacklist.BlacklistAPIClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * 查询黑名单forkjointask
 *
 * @author xingmin
 */
@Slf4j
public class ForkJoinTask extends RecursiveTask<List> {
    private static final String RISK_SUCCESS = "1";
    private static final BlacklistAPIClient RISK_CLIENT = new BlacklistAPIClient();
    private static int CAPACITY = 1000;

    /**
     * 定义最大计算容量
     */
    private int capacity;

    /**
     * 黑名单接口入参
     */
    List<BlackListDto> blackListDtos;

    public ForkJoinTask(List<BlackListDto> blackListDtos) {
        this.blackListDtos = blackListDtos;
    }

    @Override
    protected List compute() {
        if (blackListDtos.size() <= CAPACITY) {
            QueryResultDto result;
            try {
                result = RISK_CLIENT.blacklistBatchQuery1(blackListDtos);
            } catch (Exception e) {
                log.error("无法获取黑名单信息", e);
                return Lists.newArrayList();
            }

            if (ObjectUtils.isEmpty(result)) {
                return Lists.newArrayList();
            }

            if (!RISK_SUCCESS.equals(result.getCode())) {
                return Lists.newArrayList();
            }

            if (result.getModel() == null) {
                return Lists.newArrayList();
            }
            return (List) result.getModel();
        }

        int middle = blackListDtos.size() / 2;
        List<BlackListDto> l1 = Lists.newArrayList();
        List<BlackListDto> l2 = Lists.newArrayList();
        for (int i = 0; i < blackListDtos.size(); i++) {
            if (i < middle) {
                l1.add(blackListDtos.get(i));
            } else {
                l2.add(blackListDtos.get(i));
            }
        }

        ForkJoinTask task1 = new ForkJoinTask(l1);
        ForkJoinTask task2 = new ForkJoinTask(l2);
        invokeAll(task1, task2);

        List result = Lists.newArrayList();
        result.addAll(task1.join());
        result.addAll(task2.join());

        return result;
    }

}
