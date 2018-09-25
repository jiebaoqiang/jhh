
package com.jhh.dc.baika.manage.service.impl;


import com.jhh.dc.baika.entity.manager.*;
import com.jhh.dc.baika.manage.mapper.*;
import com.jhh.dc.baika.api.manager.ManageInfoService;
import com.jhh.dc.baika.entity.manager_vo.FeedbackVo;
import com.jhh.dc.baika.entity.manager_vo.MsgTemplateVo;
import com.jhh.dc.baika.entity.manager_vo.QuestionVo;
import com.jhh.dc.baika.entity.utils.ManagerResult;
import com.jhh.dc.baika.manage.mapper.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *描述：
 *@author: Wanyan
 *@date： 日期：2016年10月19日 时间：上午11:53:57
 *@version 1.0
 */

@Service
public class ManageInfoServiceImpl implements ManageInfoService {
	private static Logger log = Logger.getLogger(ManageInfoServiceImpl.class);
	@Autowired
	private CodeTypeMapper codeTypeMapper;
	/* (non-Javadoc)
	 * @see com.jhh.dc.baika.api.manager.ManageInfoService#getCodeTypeList()
	 */
	@Override
	public List<CodeType> getCodeTypeList() {

		return codeTypeMapper.getCodeTypeList();
	}

	/* (non-Javadoc)
	 * @see com.jhh.dc.baika.api.manager.ManageInfoService#deleteCodeType(java.lang.Integer)
	 */
	@Override
	public ManagerResult deleteCodeType(String idfordel) {
		String[] brroid= idfordel.split(",");
		
		ManagerResult managerResult  = new ManagerResult();
		for (int i = 0; i < brroid.length; i++) {
			try {
				int result =codeTypeMapper.deleteByPrimaryKey(Integer.parseInt(brroid[i]));
					managerResult.setCode(result);
					if(result>0){
						managerResult.setMessage("处理成功！");
					}else{
						managerResult.setMessage("处理失败！");
					}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return managerResult;
	}
	@Override
	public ManagerResult insertCodeType(CodeType record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =codeTypeMapper.insert(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}

	@Override
	public ManagerResult UpdateCodeType(CodeType record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =codeTypeMapper.updateByPrimaryKeySelective(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}
	@Autowired
	private CodeValueMapper codeValueMapper;
	/* (non-Javadoc)
	 * @see com.jhh.dc.baika.api.manager.ManageInfoService#getCodeValueListByCode(java.lang.String)
	 */
	@Override
	public List<CodeValue> getCodeValueListByCode(String code_type) {
		// TODO Auto-generated method stub
		return codeValueMapper.getCodeValueListByCode(code_type);
	}

	/* (non-Javadoc)
	 * @see com.jhh.dc.baika.api.manager.ManageInfoService#deleteCodeValue(java.lang.Integer)
	 */
	@Override
	public ManagerResult deleteCodeValue(String idfordel) {
	String[] brroid= idfordel.split(",");
		
		ManagerResult managerResult  = new ManagerResult();
		for (int i = 0; i < brroid.length; i++) {
			try {
				int result =codeValueMapper.deleteByPrimaryKey(Integer.parseInt(brroid[i]));
					managerResult.setCode(result);
					if(result>0){
						managerResult.setMessage("处理成功！");
					}else{
						managerResult.setMessage("处理失败！");
					}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return managerResult;
	}

	@Override
	public ManagerResult insertCodeValue(CodeValue record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =codeValueMapper.insert(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}

	@Override
	public ManagerResult UpdateCodeValue(CodeValue record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =codeValueMapper.updateByPrimaryKeySelective(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}
	@Autowired
	private QuestionMapper questionMapper;
	@Override
	public ManagerResult insertQuestion(Question record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =questionMapper.insert(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}

	@Override
	public ManagerResult UpdateQuestion(Question record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =questionMapper.updateByPrimaryKeySelective(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}
	/* (non-Javadoc)
	 * @see com.jhh.dc.baika.api.manager.ManageInfoService#getAllQuestionList()
	 */
	@Override
	public List<QuestionVo> getAllQuestionList() {
		// TODO Auto-generated method stub
		return questionMapper.getAllQuestionList();
	}

	@Autowired
	private MsgMapper msgMapper;
	@Override
	public ManagerResult insertMsg(Msg record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =msgMapper.insert(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}

	@Override
	public ManagerResult UpdateMsg(Msg record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =msgMapper.updateByPrimaryKeySelective(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}
	@Autowired
	private MsgTemplateMapper msgTemplateMapper;
	@Override
	public ManagerResult insertMsgTemplate(MsgTemplate record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =msgTemplateMapper.insert(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}

	@Override
	public ManagerResult UpdateMsgTemplate(MsgTemplate record) {
		ManagerResult managerResult  = new ManagerResult();
		try {
			int result =msgTemplateMapper.updateByPrimaryKeySelective(record);
				managerResult.setCode(result);
				if(result>0){
					managerResult.setMessage("处理成功！");
				}else{
					managerResult.setMessage("处理失败！");
				}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return managerResult;
	}
	/* (non-Javadoc)
	 * @see com.jhh.dc.baika.api.manager.ManageInfoService#getAllMsgTemplateList()
	 */
	@Override
	public List<MsgTemplateVo> getAllMsgTemplateList() {
		// TODO Auto-generated method stub
		return msgTemplateMapper.getAllMsgTemplateList();
	}

	/* (non-Javadoc)
	 * @see com.jhh.dc.baika.api.manager.ManageInfoService#getFeedbackList()
	 */
	@Autowired
	private FeedbackMapper feedbackMapper;
	@Override
	public List<FeedbackVo> getFeedbackList() {
		// TODO Auto-generated method stub
		return feedbackMapper.getFeedbackList();
	}

	@Resource
	private SmsTemplateMapper smsTemplateMapper;
	@Override
	public ManagerResult insertSmsTemplate(SmsTemplate record) {
		ManagerResult result = new ManagerResult();
		try {
			record.setCreateDate(new Date());
			int upd = smsTemplateMapper.insert(record);
			if(upd > 0){
				result.setCode(upd);
				result.setMessage("添加成功");
			}else{
				result.setMessage("添加失败");
			}
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			e.printStackTrace();
			log.info("添加短信模板失败:"+e.getMessage());
		}
		return result;
	}

	@Override
	public ManagerResult UpdateSmsTemplate(SmsTemplate record) {
		ManagerResult result = new ManagerResult();
		try {
			record.setUpdateDate(new Date());
			int upd = smsTemplateMapper.updateByPrimaryKeySelective(record);
			if(upd > 0){
				result.setCode(upd);
				result.setMessage("更新成功");
			}else{
				result.setMessage("更新成功");
			}
		} catch (Exception e) {
			result.setMessage(e.getMessage());
			e.printStackTrace();
			log.info("添加短信模板失败:"+e.getMessage());
		}
		return result;
	}

	@Override
	public List<SmsTemplate> getAllSmsTemplateList() {
		return smsTemplateMapper.getAllSmsTemplateList();
	}
}
