package com.jhh.dc.baika.api.app;

import com.jhh.dc.baika.api.entity.ForgetPayPwdVo;
import com.jhh.dc.baika.api.entity.LoginVo;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.PersonInfoVO;
import com.jhh.dc.baika.entity.app.Bank;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.app_vo.BorrowListVO;
import com.jhh.dc.baika.entity.bankdeposit.BankDepositCommon;
import com.jhh.dc.baika.entity.manager.Feedback;

import java.util.List;

public interface UserService {



	ResponseDo<Person> selectPersonById(String userId);

	ResponseDo<?> userFeedBack(Feedback feed);


	String setMessage(String userId, String templateId, String params);

    String setRedisData(String key,int time,String data);

    String queryRedisData(String key);

	/**
	 * 查询快捷支付绑定状态并发送验证码
	 * @param phone
	 * @param bankNum
	 * @return
	 */
	NoteResult queryBindAndSendMsg(Integer perId, String phone, String bankNum);

	Person selectByPhone(String phone);

	ResponseDo<?> updatePersonInfo(Person person);

	ResponseDo<Person> login(LoginVo vo);

	List<BorrowListVO> getBorrowListByPhone(String phone);

	/**
	 *  设置密码
	 * @param phone
     * @return
     */
    ResponseDo<?> userSetPassword(String phone, String paypwd, String confirmPaypwd);

	/**
	 *  修改支付密码
	 * @param phone
     * @return
     */
    ResponseDo<BankDepositCommon> changePassword(String phone);
	/**
	 *  重置支付密码
	 * @param phone
     * @return
     */
    ResponseDo<BankDepositCommon> resetPassword(String phone);

	/**
	 *  忘记密码
	 * @param vo
	 * @return
     */
	ResponseDo<?> forgetPayPwd(ForgetPayPwdVo vo);

    List<BorrowListVO> getBorrowListByPersonId(Integer personId);

    ResponseDo saveUserInfo(PersonInfoVO personInfoVO);

	/**
	 *  获取用户银行卡
	 * @param perId
     */
    ResponseDo<List<Bank>> getBankManagement(String perId);
}