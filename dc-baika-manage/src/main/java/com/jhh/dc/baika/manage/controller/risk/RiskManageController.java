package com.jhh.dc.baika.manage.controller.risk;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.entity.app.Reviewers;
import com.jhh.dc.baika.entity.manager.AuditsUser;
import com.jhh.dc.baika.entity.manager.ManuallyReview;
import com.jhh.dc.baika.manage.service.loan.SystemUserService;
import com.jhh.dc.baika.manage.service.risk.RiskManageService;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jhh.dc.baika.common.util.ExcelUtils;

import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
@RequestMapping("/risk")
public class RiskManageController {

	private static final Logger logger = LoggerFactory.getLogger(RiskManageController.class);

	@Autowired
    RiskManageService riskManageService;

	@Autowired
	SystemUserService systemUserService;

	/**
	 * 审核放款(审核管理)
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/auditsforUser", method = RequestMethod.GET)
	public String getauditsforUser(HttpServletRequest request) {
		QueryParamUtils.buildPage(request);
		List result = riskManageService.auditsforUser(request.getParameterMap());
		return JSON.toJSONString(new PageInfo(result));
	}

	/**
	 * 审核管理导出
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/auditsforUser/export", method = RequestMethod.POST)
	public void exportAuditsforUser(HttpServletRequest request, HttpServletResponse response) {
		QueryParamUtils.buildPage(request,100000);
		List result = riskManageService.auditsforUser(request.getParameterMap());

		Map<String, Object> map = new HashMap();
		map.put(NormalExcelConstants.FILE_NAME, DateUtil.getDateTimeString(Calendar.getInstance().getTime()));
		map.put(NormalExcelConstants.CLASS, AuditsUser.class);
		map.put(NormalExcelConstants.DATA_LIST, result);
		map.put(NormalExcelConstants.PARAMS, new ExportParams());
		ExcelUtils.jeecgSingleExcel(map, request,response);
	}

	/**
	 * 查询风控审核人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/reviewers", method = RequestMethod.GET)
	public List getReviewers(String status) {
		return riskManageService.getReviewers(status);
	}

	/**
	 * 查询所有风控人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/reviewers/queryManager", method = RequestMethod.POST)
	public List queryMananger() {
		return systemUserService.selectCollectors();
	}

	/**
	 * 修改风控审核人员状态
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/reviewers/status", method = RequestMethod.POST)
	public Response modefiyReviewersStatus(String brroIds, String status) {
		return riskManageService.modefiyReviewersStatus(brroIds, status);
	}
	/**
	 * 添加风控审核人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addReviewers", method = RequestMethod.POST)
	public Response addReviewers(String employeeInfo) {
		String employeeNum = employeeInfo.split(",")[0];
		String employeeName = employeeInfo.split(",")[1];
		Response response = riskManageService.getReviewersByEmployeeNum(employeeNum);
		if(500 == response.getCode()){
			return response;
		}
		return riskManageService.addReviewers(employeeName, employeeNum);
	}

	/**
	 * 添加风控审核人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addReviewersByObject", method = RequestMethod.POST)
	public Response addReviewers(Reviewers reviewers) {
		Response response = riskManageService.getReviewersByEmployeeNum(reviewers.getEmployNum());
		if(500 == response.getCode()){
			return response;
		}
		return riskManageService.addReviewers(reviewers);
	}

	/**
	 * 删除风控审核人员
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delReviewers", method = RequestMethod.POST)
	public Response delReviewers(String employNum) {
		return riskManageService.delReviewersByEmployNum(employNum);
	}

}

