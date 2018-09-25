package com.jhh.dc.baika.dao;

import com.jhh.dc.baika.entity.share.CommissionSummary;

import tk.mybatis.mapper.common.Mapper;

/**
 * @author xingmin
 */
public interface CommissionSummaryMapper extends Mapper<CommissionSummary>{

    /**
     * 佣金汇总
     * @return
     */
    int updateCommissionSummary();
}
