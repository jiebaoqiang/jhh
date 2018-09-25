package com.jhh.dc.baika.manage.service.risk;

import java.util.List;
import java.util.Map;

import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.entity.app.Reviewers;

public interface RiskManageService {
	/**
	 * 风险控制，待审核(审核管理)
	 * @param parameterMap
	 * @return
	 */
	List auditsforUser(Map<String, String[]> parameterMap);

	/**
	 * 查询风控审核人员
	 * @return
	 */
	List getReviewers(String status);

	/**
	 * 修改风控审核人员状态
	 * @param brroIds
	 * @param status
	 * @return
	 */
	Response modefiyReviewersStatus(String brroIds, String status);
	/**
	 * 添加风控审核人员
	 * @param employeeName
	 * @param employeeNum
	 * @return
	 */
	Response addReviewers(String employeeName, String employeeNum);

	Response addReviewers(Reviewers reviewers);

	Response delReviewersByEmployNum(String employNum);

	Response getReviewersByEmployeeNum(String employeeNum);
}
