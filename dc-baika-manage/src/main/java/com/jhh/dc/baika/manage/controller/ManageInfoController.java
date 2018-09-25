package com.jhh.dc.baika.manage.controller;


import com.jhh.dc.baika.entity.manager.*;
import com.jhh.dc.baika.manage.service.loan.SystemUserService;
import com.jhh.dc.baika.api.manager.ManageInfoService;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.manager_vo.FeedbackVo;
import com.jhh.dc.baika.entity.manager_vo.MsgTemplateVo;
import com.jhh.dc.baika.entity.manager_vo.QuestionVo;
import com.jhh.dc.baika.entity.utils.ManagerResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/info")
public class ManageInfoController {
	@Autowired
	private ManageInfoService manageInfoService;

	@Autowired
	private SystemUserService collectorsService;
	@ResponseBody
	@RequestMapping(value = "/getCodeTypeList", method = RequestMethod.POST)
	public List<CodeType> getCodeTypeList() {
		return manageInfoService.getCodeTypeList();
	}

	@ResponseBody
	@RequestMapping(value = "/deleteCodeType", method = RequestMethod.POST)
	public ManagerResult deleteCodeType(String idfordel) {

		return manageInfoService.deleteCodeType(idfordel);
	}

	@ResponseBody
	@RequestMapping(value = "/insertCodeType", method = RequestMethod.POST)
	public ManagerResult insertCodeType(CodeType record) {

		return manageInfoService.insertCodeType(record);
	}

	@ResponseBody
	@RequestMapping(value = "/UpdateCodeType", method = RequestMethod.POST)
	public ManagerResult UpdateCodeType(CodeType record) {

		return manageInfoService.UpdateCodeType(record);
	}

	@ResponseBody
	@RequestMapping(value = "/getCodeValueListByCode", method = RequestMethod.POST)
	public List<CodeValue> getCodeValueListByCode(String code_type) {
		List<CodeValue> list=manageInfoService.getCodeValueListByCode(code_type);
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/deleteCodeValue", method = RequestMethod.POST)
	public ManagerResult deleteCodeValue(String idfordel) {
		return manageInfoService.deleteCodeValue(idfordel);
	}

	@ResponseBody
	@RequestMapping(value = "/insertCodeValue", method = RequestMethod.POST)
	public ManagerResult insertCodeValue(CodeValue record) {

		return manageInfoService.insertCodeValue(record);
	}

	@ResponseBody
	@RequestMapping(value = "/UpdateCodeValue", method = RequestMethod.POST)
	public ManagerResult UpdateCodeValue(CodeValue record) {

		// TODO Auto-generated method stub
		return manageInfoService.UpdateCodeValue(record);
	}

	@ResponseBody
	@RequestMapping(value = "/insertQuestion", method = RequestMethod.POST)
	public ManagerResult insertQuestion(Question record) {

		record.setUpdateDate(new Date());
		record.setCreateTime(new Date());
		return manageInfoService.insertQuestion(record);
	}

	@ResponseBody
	@RequestMapping(value = "/UpdateQuestion", method = RequestMethod.POST)
	public ManagerResult UpdateQuestion(Question record) {
		return manageInfoService.UpdateQuestion(record);
	}

	@ResponseBody
	@RequestMapping(value = "/getAllQuestionList", method = RequestMethod.POST)
	public List<QuestionVo> getAllQuestionList() {
		// TODO Auto-generated method stub
		return manageInfoService.getAllQuestionList();
	}

	@ResponseBody
	@RequestMapping(value = "/insertMsg", method = RequestMethod.POST)
	public ManagerResult insertMsg() {
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/UpdateMsg", method = RequestMethod.POST)
	public ManagerResult UpdateMsg() {
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/insertMsgTemplate", method = RequestMethod.POST)
	public ManagerResult insertMsgTemplate(MsgTemplate record) {
		record.setUpdateDate(new Date());
		record.setCreateTime(new Date());
		return manageInfoService.insertMsgTemplate(record);
	}

	@ResponseBody
	@RequestMapping(value = "/UpdateMsgTemplate", method = RequestMethod.POST)
	public ManagerResult UpdateMsgTemplate(MsgTemplate record) {
		return manageInfoService.UpdateMsgTemplate(record);
	}

	@ResponseBody
	@RequestMapping(value = "/getAllMsgTemplateList", method = RequestMethod.POST)
	public List<MsgTemplateVo> getAllMsgTemplateList() {
		return manageInfoService.getAllMsgTemplateList();
	}

	@ResponseBody
	@RequestMapping(value = "/getFeedbackList", method = RequestMethod.POST)
	public List<FeedbackVo> getFeedbackList() {
		return manageInfoService.getFeedbackList();
	}
	/**
	 * 更新短信模板
	 * */
	@ResponseBody
	@RequestMapping(value = "/UpdateSmsTemplate", method = RequestMethod.POST)
	public ManagerResult UpdateSmsTemplate(SmsTemplate record) {
		return manageInfoService.UpdateSmsTemplate(record);
	}
	/**
	 * 查询短信模板
	 * */
	@ResponseBody
	@RequestMapping(value = "/getAllSmsTemplateList", method = RequestMethod.POST)
	public List<SmsTemplate> getAllSmsTemplateList() {
		return manageInfoService.getAllSmsTemplateList();
	}
	/**
	 * 添加短信模板
	 * */
	@ResponseBody
	@RequestMapping(value = "/addSmsTemplate", method = RequestMethod.POST)
	public ManagerResult AddSmsTemplate(SmsTemplate smsTemplate) {
		return manageInfoService.insertSmsTemplate(smsTemplate);
	}

	/**
	 * 获取渠道商扣量百分比列表
	 */
	@ResponseBody
	@RequestMapping(value = "/queryChannelCollectors", method = RequestMethod.POST)
	public List<SystemUser> queryChannelCollectors() {
		List<SystemUser> list=collectorsService.queryChannelCollectors();
		return list;
	}

	/**
	 * 更新渠道商扣量百分比
	 */
	@ResponseBody
	@RequestMapping(value = "/updateChannelPercent", method = RequestMethod.POST)
	public ManagerResult updateChannelPercent(SystemUser collectors) {
		return collectorsService.updateChannelPercent(collectors);
	}

}
