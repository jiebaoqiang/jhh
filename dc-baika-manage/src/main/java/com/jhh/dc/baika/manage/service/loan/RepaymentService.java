package com.jhh.dc.baika.manage.service.loan;

import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface RepaymentService {
    List getRepaymentPlan(Map<String, String[]> parameterMap);

    PageInfo getRepaymentOrder(HttpServletRequest request);

    PageInfo getRepaymentOrder(HttpServletRequest request, int size);

    List selectCollectorsList(Map<String, String[]> parameterMap, String userId);

    String getSurplusTotalAmountNum(String contractId);

}
