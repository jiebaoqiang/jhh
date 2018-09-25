package com.jhh.dc.baika.manage.mapper;


import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.entity.manager_vo.LoanManagementVo;

public interface RepaymentMapper {

    /**
     * 还款计划
     * @param map
     * @returnF
     */
    List getRepaymentPlan(Map map);

    /**
     * 还款流水
     * @param map
     * @returnF
     */
    List getRepaymentOrder(Map map);
    /**
     * 还款流水总条数
     * @param queryMap
     * @returnF
     */
    long getRepaymentOrderCount(Map<String, Object> queryMap);

    /**
     * 贷后信息查询
     * @param arg
     * @return
     */
    List<LoanManagementVo> getLoanManagement(Map<String, Object> arg);

    /**
     * 查询贷后管理总条数
     * @param queryMap
     * @return
     */
    long getLoanManagementCount(Map<String, Object> queryMap);

    List selectCollectorsList(Map map);

    List getBatchReduce(Map conditions);

    List getBatchReduceList(Map conditions);

    //获取应还总额
    String getSurplusTotalAmountNum(String contractId);
}