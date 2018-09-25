package com.jhh.dc.baika.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.pojo.auth.Role;
import com.jhh.dc.baika.manage.service.refund.RefundReviewService;
import com.jhh.dc.baika.manage.utils.Assertion;
import com.jhh.dc.baika.manage.utils.MD5Util;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.capital.AgentRefundRequest;
import com.jhh.dc.baika.api.sms.SmsService;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.entity.common.ResponseCode;
import com.jhh.dc.baika.entity.refund.RefundReview;
import com.jhh.dc.baika.entity.refund.RefundReviewVo;
import com.jhh.dc.baika.manage.mapper.PersonMapper;
import com.jhh.dc.baika.manage.mapper.RefundReviewMapper;
import com.jhh.dc.baika.common.enums.MsgTemplateEnum;
import com.jhh.dc.baika.common.enums.SmsTemplateEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.common.util.thread.runner.AbstractSimpleRunner;
import com.jinhuhang.settlement.dto.SettlementResult;
import com.jinhuhang.settlement.service.SettlementAPI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.MapperException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RefundReviewServiceImpl implements RefundReviewService {

    private static Logger log = LoggerFactory.getLogger(RefundReviewService.class);
    @Autowired
    private RefundReviewMapper refundReviewMapper;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserService userService;
    @Autowired
    SettlementAPI settlementAPI;
    @Autowired
    AgentChannelService agentChannelService;

    /**
     * 退款列表
     * @param
     * @returnF
     */
    @Override
    public PageInfo<RefundReviewVo> queryRefundReviewList(HttpServletRequest request, int offset, int size,String auth) {
        Map<String, Object> param = QueryParamUtils.getParams(request.getParameterMap());
        handleMapDate(param);
        if(null!=auth){
            String md5= MD5Util.encryptStringBySign(Role.POST_LOAN_FINANCE.getType()+"."+Role.POST_LOAN_FINANCE.getLevel());
            if(auth.equals(md5)){
              param.put("statusNotEq","-1");
            }
        }
        //分页设置总条数
        PageHelper.offsetPage(offset, size);
        List<RefundReviewVo> list=refundReviewMapper.queryRefundReviewList(param);
        PageInfo<RefundReviewVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    private void handleMapDate(Map<String,Object> map) {
        if(null!=map&&map.size()>0){
            for (String entry : map.keySet()) {
                if (entry.contains("createDate")) {
                    map.put(entry, DateUtil.stampToDate(map.get(entry).toString()));
                }
            }
        }
    }

    @Override @Transactional
    public Response affirmRefund(Integer perId, String operator) {
        log.info("affirmRefund perId:" + perId + "----operator:" + operator);
        Assertion.isPositive(perId, "用户Id不能为空");
        Response response = new Response().code(ResponseCode.FIAL).msg("操作失败");
        Person person = personMapper.selectByPrimaryKey(perId);
        if(person != null){
            Assertion.isPositive(person.getBalance().compareTo(BigDecimal.ZERO),"用户余额不足，无法退款");
            //调用清结算更新余额接口
            log.info("affirmRefund更新余额 perId:" + perId);
            SettlementResult result = settlementAPI.changeBlance(perId, person.getBalance(),Constants.ChangeBlanceType.DEDUCTBLANCE);
            if(result != null && result.getCode() == 1){
                RefundReview refundReview = new RefundReview();
                refundReview.setPerId(perId);
                refundReview.setAmount(person.getBalance());
                refundReview.setStatus(Constants.refundStatus.AFFIRM);
                refundReview.setBankName(person.getBankName());
                refundReview.setBankNum(person.getBankCard());
                refundReview.setCreateUser(operator);
                refundReview.setCreateDate(Calendar.getInstance().getTime());
                refundReview.setUpdateDate(new Date());
                refundReviewMapper.insertSelective(refundReview);
                response.msg("操作成功").code(ResponseCode.SUCCESS);
            }
        }
        return response;
    }

    @Override
    public Response refund(Integer refundId, String remark, String operator) {
        log.info("refund refundId:" + refundId + "---remark:" + remark + "----operator:" + operator);
        Assertion.isPositive(refundId, "审核Id不能为空");
        Response response = new Response().code(ResponseCode.FIAL).msg("操作失败");
        RefundReview refundReview = refundReviewMapper.selectByPrimaryKey(refundId);
        if(refundReview != null){
            Assertion.isTrue((refundReview.getStatus() == Constants.refundStatus.AFFIRM || refundReview.getStatus() == Constants.refundStatus.FAIL),
                    "只有已确认和退款失败状态才允许退款");

            //调用退款接口
            AgentRefundRequest agentrefundRequest = new AgentRefundRequest(refundReview.getPerId(),
                    refundReview.getAmount(),0,"",refundReview.getBankNum());

            ResponseDo result = agentChannelService.refund(agentrefundRequest);

            if(result.getCode() == 200){
                //更新流水号，和审核状态
                refundReview.setOrderId(result.getData().toString());
                refundReview.setStatus(Constants.refundStatus.ING);
                refundReview.setRemark(remark);
                refundReview.setReviewUser(operator);
                refundReview.setReviewDate(new Date());
                refundReview.setUpdateDate(new Date());
                refundReviewMapper.updateByPrimaryKeySelective(refundReview);
                response.code(ResponseCode.SUCCESS).msg("操作成功");
            }else {
                response.code(ResponseCode.SUCCESS).msg(result.getInfo());
            }
        }
        return response;
    }

    @Override
    public Response rejectRefund(Integer refundId, String remark,String operator) {
        log.info("rejectRefund refundId:" + refundId + "---remark:" + remark );
        Response response = new Response().code(ResponseCode.FIAL).msg("操作失败");
        Assertion.isPositive(refundId, "审核Id不能为空");
        RefundReview refundReview = refundReviewMapper.selectByPrimaryKey(refundId);
        if(refundReview != null) {
            Assertion.isTrue((refundReview.getStatus() == Constants.refundStatus.AFFIRM || refundReview.getStatus() == Constants.refundStatus.FAIL),
                    "只有已确认和退款失败状态才允许驳回");

            // 调用清结算更新余额接口
            log.info("affirmRefund更新余额 perId:" + refundReview.getPerId());
            SettlementResult result = settlementAPI.changeBlance(refundReview.getPerId(), refundReview.getAmount(),Constants.ChangeBlanceType.ADDBLANCE);
            if(result != null && result.getCode() == 1){
                refundReview.setStatus(Constants.refundStatus.REUBJECT);
                refundReview.setRemark(remark);
                refundReview.setReviewUser(operator);
                refundReview.setReviewDate(new Date());
                refundReview.setUpdateDate(new Date());
                refundReviewMapper.updateByPrimaryKeySelective(refundReview);
                response.code(ResponseCode.SUCCESS).msg("操作成功");
            }
        }
        return response;
    }


    @Override
    public Response callback(String perId, String status,String serialNo) {

        log.info("退款回调入参 perId = {},status = {}, serialNo = {}",perId,status,serialNo);
        Person person = personMapper.selectByPrimaryKey(Integer.parseInt(perId));

        if(ObjectUtils.isEmpty(person)){
            return new Response().code(CodeReturn.fail).msg("用户不存在");
        }
        RefundReview review = refundReviewMapper.selectByOrderIdAndPerId(serialNo,Integer.parseInt(perId));

        if(ObjectUtils.isEmpty(review)){
            return new Response().code(CodeReturn.fail).msg("未知审核退款记录");
        }

        RefundReview updateRefundReview = new RefundReview();

        updateRefundReview.setId(review.getId());
        updateRefundReview.setStatus("0".equals(status) ? Constants.refundStatus.FAIL:Constants.refundStatus.SUCESS);
        updateRefundReview.setUpdateDate(new Date());
        refundReviewMapper.updateByPrimaryKeySelective(updateRefundReview);
        // 去掉发送短信功能
//         if("0".equals(status)){
//
//            asyncSendSms(person,SmsTemplateEnum.REFUND_FAIL.getCode());
//            asyncSendMessage(person, MsgTemplateEnum.REFUND_FAIL.getCode());
//        } else {
//             asyncSendMessage(person, MsgTemplateEnum.REFUND_SUCCESS.getCode());
//         }
        return new Response().code(CodeReturn.success).msg(StateCode.SUCCESS_MSG);
    }

    /**
     * 发送站内信(异步操作)
     * @param person
     * @param templateId
     */
    private void asyncSendMessage(Person person,Integer templateId){
        AsyncExecutor.execute(new SendMessage(person,templateId));
    }

    /**
     * 异步发送短信
     * @param person
     * @param templateId
     */
    private void asyncSendSms(Person person,Integer templateId){
        AsyncExecutor.execute(new SendSms(person,templateId));
    }

    class SendMessage extends AbstractSimpleRunner {

        private Person person;

        private Integer templateId;

        public SendMessage(Person person, Integer templateId) {
            this.person = person;
            this.templateId = templateId;
        }

        @Override
        public boolean doExecute() {
            try {
                log.info("-----------> send message param【userId: %s, templateId: %s, params: %s】", person.getId(), templateId, person.getName());
                String result = userService.setMessage(Integer.toString(person.getId()), Integer.toString(templateId), person.getName());
                JSONObject json = JSONObject.parseObject(result);
                if (!StringUtils.equals(CodeReturn.SUCCESS_CODE, json.getString(Response.ResponseEnum.code.toString()))) {
                    log.error(String.format("-----------> send message fail! param【userId: %s, templateId: %s, params: %s】 result【%s】", person.getId(), templateId, person.getName(), json.getString("info")));
                    return false;
                }
                return true;
            } catch (Exception e) {
                log.error("Exception happens when send message ", e);
                return false;
            }
        }
    }

    class SendSms extends AbstractSimpleRunner {

        private Person person;

        private Integer templateId;

        public SendSms(Person person, Integer templateId) {
            this.person = person;
            this.templateId = templateId;
        }

        @Override
        public boolean doExecute() {

            try {
                log.info(String.format("-----------> send sms param【userId: %s, phone: %s, templateId: %s, params: %s】", person.getId(), person.getPhone(), templateId, person.getName()));
                ResponseDo responseDo = smsService.sendSms(templateId, person.getPhone());
                if (StateCode.SUCCESS_CODE != responseDo.getCode()) {
                    log.warn("-----------> send sms fail! param【userId: %s, phone: %s, templateId: %s, params: %s】 result【%s】", person.getId(), person.getPhone(), templateId, person.getName(), responseDo.getInfo());
                    return false;
                }
                return true;
            } catch (Exception e) {
                log.error("Exception happens when send sms ", e);
                return false;
            }
        }
    }

}
