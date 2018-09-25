package com.jhh.dc.baika.api.loan;

import java.util.List;

import com.jhh.dc.baika.entity.manager.RepaymentPlan;

/**
 * Created by chenchao on 2018/1/10.
 */
public interface RepaymentPlanService {

    List getRepaymentTermPlan(String borrowId);

    List<RepaymentPlan> getOverdueRepaymentPlan(String borrId);
}
