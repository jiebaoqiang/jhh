
package com.jhh.dc.baika.api.manager;

import java.util.List;

import com.jhh.dc.baika.entity.manager.*;
import com.jhh.dc.baika.entity.manager_vo.FeedbackVo;
import com.jhh.dc.baika.entity.manager_vo.MsgTemplateVo;
import com.jhh.dc.baika.entity.manager_vo.QuestionVo;
import com.jhh.dc.baika.entity.utils.ManagerResult;

/**
 *描述：
 *@author: Wanyan
 *@date： 日期：2016年10月18日 时间：下午2:51:11
 *@version 1.0
 */
public interface ManageInfoService {
	
	List<CodeType> getCodeTypeList();
	ManagerResult deleteCodeType(String idfordel);
	ManagerResult insertCodeType(CodeType record);
	ManagerResult UpdateCodeType(CodeType record);
	
	List<CodeValue> getCodeValueListByCode(String code_type);
    ManagerResult deleteCodeValue(String idfordel);
	ManagerResult insertCodeValue(CodeValue record);
	ManagerResult UpdateCodeValue(CodeValue record);
	
	ManagerResult insertQuestion(Question record);
	ManagerResult UpdateQuestion(Question record);
	List<QuestionVo> getAllQuestionList();
	
	ManagerResult insertMsg(Msg record);
	ManagerResult UpdateMsg(Msg record);
	
	ManagerResult insertMsgTemplate(MsgTemplate record);
	ManagerResult UpdateMsgTemplate(MsgTemplate record);
	List<MsgTemplateVo> getAllMsgTemplateList();
	
	List<FeedbackVo> getFeedbackList();

	ManagerResult insertSmsTemplate(SmsTemplate record);
	ManagerResult UpdateSmsTemplate(SmsTemplate record);
	List<SmsTemplate> getAllSmsTemplateList();
}
