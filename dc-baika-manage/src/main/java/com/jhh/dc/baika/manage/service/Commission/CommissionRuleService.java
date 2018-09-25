package com.jhh.dc.baika.manage.service.Commission;

import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.entity.share.CommissionRule;
import com.jhh.dc.baika.entity.utils.ManagerResult;

import java.util.Map;

public interface CommissionRuleService {

    /**
     * 获取规则列表
     * @return
     */
    PageInfo<CommissionRule> queryCommissionRuleList(Map<String, Object> queryMap, int offset, int size);

    /**
     * 获取单个规则
     * @param id
     * @return
     */
    CommissionRule   selectByPrimaryKey(Integer id);

    /**
     * 删除规则列表
     * @param idfordel
     * @return
     */
    ManagerResult deleteCommissionRule(String idfordel);

    /**
     * 新增规则列表
     * @param commissionRule
     * @return
     */
    ManagerResult insertCommissionRule(CommissionRule commissionRule);

    /**
     * 修改规则列表
     * @param commissionRule
     * @return
     */
    ManagerResult updateCommissionRule(CommissionRule commissionRule);
}
