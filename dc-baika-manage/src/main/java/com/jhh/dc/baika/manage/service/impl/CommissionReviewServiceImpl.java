package com.jhh.dc.baika.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.entity.SimpleResp;
import com.jhh.dc.baika.manage.mapper.*;
import com.jhh.dc.baika.manage.service.Commission.CommissionReviewService;
import com.jhh.dc.baika.manage.utils.Detect;
import com.jhh.dc.baika.api.app.UserService;
import com.jhh.dc.baika.api.channel.WithdrawalService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.entity.cash.WithdrawalVo;
import com.jhh.dc.baika.api.sms.SmsService;
import com.jhh.dc.baika.entity.app.BankVo;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.enums.CommissionOrderStatusEnum;
import com.jhh.dc.baika.entity.enums.CommissionReviewStatusEnum;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.manager_vo.CommissionDetailVo;
import com.jhh.dc.baika.entity.share.CommissionReview;
import com.jhh.dc.baika.entity.share.CommissionSummary;
import com.jhh.dc.baika.manage.mapper.*;
import com.jhh.dc.baika.common.enums.MsgTemplateEnum;
import com.jhh.dc.baika.common.enums.PayTriggerStyleEnum;
import com.jhh.dc.baika.common.enums.SmsTemplateEnum;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jhh.dc.baika.common.util.thread.AsyncExecutor;
import com.jhh.dc.baika.common.util.thread.runner.AbstractRetryRunner;
import com.jhh.dc.baika.common.util.thread.runner.AbstractSimpleRunner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xingmin
 */
@Service
public class CommissionReviewServiceImpl implements CommissionReviewService {
    private static Logger LOG = Logger.getLogger(CommissionReviewServiceImpl.class);

    @Autowired
    private CommissionReviewMapper commissionReviewMapper;

    @Autowired
    private CommissionOrderMapper commissionOrderMapper;

    @Autowired
    private CommissionSummaryMapper commissionSummaryMapper;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private BankInfoMapper bankInfoMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SystemUserMapper collectorsMapper;

    private static final String QUERY_SELECTOR = "selector";
    private static final String QUERY_DESC = "desc";
    private static final String CONSTANT_1 = "1";

    /**
     * 获取获取审核结果列表
     */
    @Override
    public PageInfo<CommissionReview> queryCommissionReviewList(Map<String, Object> queryMap, int offset, int size, boolean isReview) {
        if(Detect.notEmpty(queryMap)){
            if(Detect.notEmpty(queryMap.get("employNum") + "")){
                String userNo = collectorsMapper.getSysNoByName(queryMap.get("employNum").toString());
                if(Detect.notEmpty(userNo)){
                    queryMap.put("employNum",userNo);
                }
            }
        }
        Example queryExample = new Example(CommissionReview.class);
        Example.Criteria criteria = queryExample.createCriteria();
        if (isReview) {
            // 删除放款中和放款失败的状态，审核结果列表不需要此两种状态，只有审核拒绝和放款成功
            criteria.andIn("status", Arrays.asList( CommissionReviewStatusEnum.REJECT_REVIEW.getStatus(), CommissionReviewStatusEnum.AGREE_AND_PAY_SUCCESS.getStatus()));
        } else {
            criteria.andIn("status", Arrays.asList(CommissionReviewStatusEnum.NO_REVIEW.getStatus(),CommissionReviewStatusEnum.AGREE_REVIEW.getStatus(),CommissionReviewStatusEnum.AGREE_BUT_PAY_FAIL.getStatus()));
        }

        handleCondition(queryMap, criteria);
        if (queryMap.containsKey(QUERY_SELECTOR) && queryMap.containsKey(QUERY_DESC)) {
            Example.OrderBy orderBy = queryExample.orderBy(queryMap.get(QUERY_SELECTOR).toString());
            if (QUERY_DESC.equals(queryMap.get(QUERY_DESC))) {
                orderBy.desc();
            }
        }
        PageHelper.offsetPage(offset, size);
        List<CommissionReview> commissionReviews = commissionReviewMapper.selectByExample(queryExample);
        if(commissionReviews!=null&&commissionReviews.size()>0)
            for(int i=0;i<commissionReviews.size();i++){
                CommissionReview commissionReview=commissionReviews.get(i);
                if(null!=commissionReview.getEmployNum()&&!"".equals(commissionReview.getEmployNum())) {
                    List<SystemUser> list = collectorsMapper.selectUserBySysNo(commissionReview.getEmployNum());
                    if(null!=list&&list.size()>0)
                        commissionReviews.get(i).setEmployNum(list.get(0).getUserName());
                    else
                        commissionReviews.get(i).setEmployNum("");
                }
            }
        return new PageInfo<>(commissionReviews);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateCommissionReviewResult(String reviewId, String reviewResult, String reviewReason, String userId) {
        CommissionReview review = commissionReviewMapper.selectByPrimaryKey(Integer.parseInt(reviewId));
        if (review == null) {
            LOG.info(String.format("-----updateCommissionReviewResult-----> records not found 【reviewId: %s】", reviewId));
            return new Response().getResponse(-100, "未查询到相关记录", "");
        }

        // 1 通过  2 拒绝
        review.setStatus(CONSTANT_1.equals(reviewResult) ? CommissionReviewStatusEnum.AGREE_REVIEW.getStatus() : CommissionReviewStatusEnum.REJECT_REVIEW.getStatus());
        review.setReason(reviewReason);
        review.setReviewDate(Date.from(ZonedDateTime.now().toInstant()));
        review.setEmployNum(userId);
        try {
            // 更新用户佣金领取审核表
            int reviewCount = commissionReviewMapper.updateByPrimaryKeySelective(review);
            LOG.info(String.format("-----updateCommissionReviewResult-----> update commissionReview param【%s】 result【%s】", review, reviewCount));
            if (reviewCount < 1) {
                return new Response().getResponse(200, String.format("审核完成, 共【%s】条记录", reviewCount), "");
            }

            // 更新用户佣金获取流水表( 审核通过，order更新为 2 待放款；审核拒绝，order更新为 0 未领取)
            int orderCount = commissionOrderMapper.updateWithDrawalStatus(CommissionReviewStatusEnum.REJECT_REVIEW.getStatus() == review.getStatus() ? CommissionOrderStatusEnum.UNRECEIVED.getStatusString() : CommissionOrderStatusEnum.PENDING.getStatusString(), Arrays.asList(review.getCommissionOrderIds().split(",")));
            LOG.info(String.format("-----updateCommissionReviewResult-----> update commission order records【%s】, withDrawStatus【%s】", orderCount, CommissionReviewStatusEnum.REJECT_REVIEW.getStatus() == review.getStatus() ? CommissionOrderStatusEnum.UNRECEIVED.getStatusString() : CommissionOrderStatusEnum.PENDING.getStatusString()));
            if (orderCount < 1) {
                throw new RuntimeException("未查询到佣金流水记录");
            }

            // 更新用户佣金汇总表(审核拒绝后，重新计算summary中的未领取余额)
            if (CommissionReviewStatusEnum.REJECT_REVIEW.getStatus() == review.getStatus()) {
                LOG.info(String.format("-----updateCommissionReviewResult-----> commissionReview status【%s】", review.getStatus()));

                CommissionSummary commissionSummary = new CommissionSummary();
                commissionSummary.setPerId(Integer.parseInt(review.getPerId()));

                LOG.info(String.format("-----updateCommissionReviewResult-----> selectOne commissionSummary param【%s】", commissionSummary));
                commissionSummary = commissionSummaryMapper.selectOne(commissionSummary);
                LOG.info(String.format("-----updateCommissionReviewResult-----> selectOne commissionSummary result【%s】", commissionSummary));
                if (commissionSummary == null) {
                    throw new RuntimeException("-----updateCommissionReviewResult-----> select commission summary records is null");
                }

                Map<String, BigDecimal> map = commissionOrderMapper.queryTotalAmount(review.getPerId());
                LOG.info(String.format("-----updateCommissionReviewResult-----> query total amount perId【%s】 result【%s】", review.getPerId(), map.get("amount")));
                commissionSummary.setCommissionBalance(map.get("amount"));

                int count = commissionSummaryMapper.updateByPrimaryKeySelective(commissionSummary);
                LOG.info(String.format("-----updateCommissionReviewResult-----> update commissionSummary param【%s】 result【%s】", commissionSummary, count));

                // 异步发送站内信(审核拒绝)
                asyncSendMessage(review, Integer.toString(MsgTemplateEnum.COMM_APPROVE_FAIL.getCode()));

                // 异步发送短信(审核拒绝)
                asyncSendSms(review, SmsTemplateEnum.COMM_APPROVE_FAIL.getCode());
            }

            // 异步放款
            asyncDoCommissionReviewLoan(reviewId, reviewResult);

            return new Response().getResponse(200, String.format("审核完成, 共【%s】条记录", reviewCount), "");
        } catch (Exception e) {
            LOG.info(String.format("-----updateCommissionReviewResult-----> update commission order exception【%s】", e.getMessage()));
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NoteResult updateCommissionPayResult(String reviewId, String status) {
        CommissionReview review = commissionReviewMapper.selectByPrimaryKey(Integer.parseInt(reviewId));
        if (review == null) {
            LOG.info(String.format("------updateCommissionPayResult-----> records not found 【reviewId: %s】", reviewId));
            return NoteResult.FAIL_RESPONSE(String.format("未查询到相关记录 reviewId【%s】", reviewId));
        }

        review.setStatus(CONSTANT_1.equals(status) ? CommissionReviewStatusEnum.AGREE_AND_PAY_SUCCESS.getStatus() : CommissionReviewStatusEnum.AGREE_BUT_PAY_FAIL.getStatus());

        try {
            // 更新用户佣金领取审核表
            int reviewCount = commissionReviewMapper.updateByPrimaryKeySelective(review);
            LOG.info(String.format("------updateCommissionPayResult-----> update commissionReview param【%s】 result【%s】", review, reviewCount));
            if (reviewCount < 1) {
                return NoteResult.SUCCESS_RESPONSE();
            }

            // 更新用户佣金获取流水表( 放款成功，order更新为 3 已放款；放款失败，order更新为 2 待放款)
            int orderCount = commissionOrderMapper.updateWithDrawalStatus(CommissionReviewStatusEnum.AGREE_AND_PAY_SUCCESS.getStatus() == review.getStatus() ? CommissionOrderStatusEnum.LOANED.getStatusString() : CommissionOrderStatusEnum.PENDING.getStatusString(), Arrays.asList(review.getCommissionOrderIds().split(",")));
            if (orderCount < 1) {
                throw new RuntimeException(String.format("------updateCommissionPayResult-----> update commission order records【%s】, loanStatus【%s】", orderCount, CommissionReviewStatusEnum.AGREE_AND_PAY_SUCCESS.getStatus() == review.getStatus() ? CommissionOrderStatusEnum.LOANED.getStatusString() : CommissionOrderStatusEnum.PENDING.getStatusString()));
            }

            // 异步发送站内信(放款成功)
            if (CONSTANT_1.equals(status)) {
                asyncSendMessage(review, Integer.toString(MsgTemplateEnum.COMM_APPROVE_SUCCESS.getCode()));
            }

            return NoteResult.SUCCESS_RESPONSE();
        } catch (Exception e) {
            LOG.info(String.format("------updateCommissionPayResult-----> update commission order exception【%s】", e.getMessage()));
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据用户id获取佣金审核详情
     *
     * @param personId 用户id
     */
    @Override
    public List<CommissionDetailVo> commissionDetailByPersonId(String personId) {
        List<CommissionDetailVo> result = new ArrayList<>();
        // 根据用户id查询一级邀请人
        List<CommissionDetailVo> level1List = commissionOrderMapper.queryLevel1CommissionOrderByPersonId(personId, null);
        level1List.forEach(commissionDetailVo -> {
            result.add(commissionDetailVo);
            List<CommissionDetailVo> level2List = commissionOrderMapper.queryLevel1CommissionOrderByPersonId(String.valueOf(commissionDetailVo.getPerId()),null);
            level2List.forEach(commissionDetailVo2 -> commissionDetailVo2.setPhone("    ".concat(commissionDetailVo2.getPhone())));
            result.addAll(level2List);
        });
        return result;
    }

    @Override
    public SimpleResp<List<CommissionDetailVo>> queryCommissionReviewDetail(String perId, String perId2, int offset, int size, String level, String phone) {
        SimpleResp<List<CommissionDetailVo>> resp = new SimpleResp<>();
        List<CommissionDetailVo> vos;
        PageHelper.offsetPage(offset, size);
        if (StringUtils.equals(Integer.toString(Constants.InviterLevel.LEVEL1), level)) {
            vos = commissionOrderMapper.queryLevel1CommissionOrderByPersonId(perId, phone);
        } else {
            vos = commissionOrderMapper.queryLevel1CommissionOrderByPersonId(perId2,null);
        }

        for (CommissionDetailVo vo : vos) {
            // 设置手机设备
            if ("android".equals(vo.getType())) {
                vo.setType("安卓");
            } else if ("ios".equals(vo.getType())) {
                vo.setType("苹果");
            }

        }
        PageInfo<CommissionDetailVo> pageInfo = new PageInfo<>(vos);

        resp.setTotal(pageInfo.getTotal());
        resp.setRecord(pageInfo.getList());
        return resp;
    }

    /**
     * 发送短信(异步操作)
     */
    private void asyncSendSms(CommissionReview review, Integer templateId) {
        LOG.info("-----------> into asyncSendMessage........");
        AsyncExecutor.execute(new SendSmsRunner(review, templateId));
    }

    /**
     * 发送站内信(异步操作)
     */
    private void asyncSendMessage(CommissionReview review, String templateId) {
        LOG.info("-----------> into asyncSendMessage........");
        AsyncExecutor.execute(new SendMessageRunner(review, templateId));
    }


    /**
     * 审核通过，放款操作(异步操作)
     */
    private void asyncDoCommissionReviewLoan(String reviewId, String reviewResult) {
        LOG.info("-----------> into asyncDoCommissionReviewLoan........");
        if (!CommissionReviewStatusEnum.AGREE_REVIEW.getStatusString().equals(reviewResult)) {
            LOG.info(String.format("-----------> commission review【id: %s】 status is 【%s】, loan reject........", reviewId, reviewResult));
            return;
        }
        AsyncExecutor.execute(new CommissionReviewRunner(reviewId));
    }

    /**
     * 发送短信runner
     */
    private final class SendSmsRunner extends AbstractRetryRunner {
        private CommissionReview commissionReview;
        private Integer templateId;

        SendSmsRunner(CommissionReview commissionReview, Integer templateId) {
            this.commissionReview = commissionReview;
            this.templateId = templateId;
        }

        @Override
        public boolean doExecute() {
            try {
                Person person = personMapper.selectByPrimaryKey(Integer.parseInt(commissionReview.getPerId()));
                LOG.info(String.format("-----------> send sms param【userId: %s, phone: %s, templateId: %s, params: %s】", person.getId(), person.getPhone(), templateId, person.getName()));
                ResponseDo responseDo = smsService.sendSms(templateId, person.getPhone());
                if (StateCode.SUCCESS_CODE != responseDo.getCode()) {
                    LOG.error(String.format("-----------> send sms fail! param【userId: %s, phone: %s, templateId: %s, params: %s】 result【%s】", person.getId(), person.getPhone(), templateId, person.getName(), responseDo.getInfo()));
                    return false;
                }
                return true;
            } catch (Exception e) {
                LOG.error("Exception happens when send sms ", e);
                return false;
            }
        }

        @Override
        public String toString() {
            return "SendSmsRunner{" + "commissionReview=" + commissionReview +
                    ", templateId=" + templateId +
                    '}';
        }
    }

    /**
     * 发送站内信runner
     */
    private final class SendMessageRunner extends AbstractRetryRunner {

        private CommissionReview commissionReview;

        private String templateId;

        SendMessageRunner(CommissionReview commissionReview, String templateId) {
            this.commissionReview = commissionReview;
            this.templateId = templateId;
        }

        @Override
        public boolean doExecute() {
            try {
                Person person = personMapper.selectByPrimaryKey(Integer.parseInt(commissionReview.getPerId()));
                LOG.info(String.format("-----------> send message param【userId: %s, templateId: %s, params: %s】", person.getId(), templateId, person.getName()));
                String result = userService.setMessage(Integer.toString(person.getId()), templateId, person.getName());
                JSONObject json = JSONObject.parseObject(result);
                if (!StringUtils.equals(CodeReturn.SUCCESS_CODE, json.getString(Response.ResponseEnum.code.toString()))) {
                    LOG.error(String.format("-----------> send message fail! param【userId: %s, templateId: %s, params: %s】 result【%s】", person.getId(), templateId, person.getName(), json.getString("info")));
                    return false;
                }
                return true;
            } catch (Exception e) {
                LOG.error("Exception happens when send message ", e);
                return false;
            }
        }

        @Override
        public String toString() {
            return "SendMessageRunner{" + "commissionReview=" + commissionReview +
                    ", templateId='" + templateId + '\'' +
                    '}';
        }
    }

    /**
     * 放款runner
     */
    private final class CommissionReviewRunner extends AbstractSimpleRunner {

        private String reviewId;

        CommissionReviewRunner(String reviewId) {
            this.reviewId = reviewId;
        }

        @Override
        public boolean doExecute() {
            try {
                // 2秒后执行放款操作
                Thread.sleep(2000);

                LOG.info("-----------> CommissionReviewRunner run begin........");
                CommissionReview review = commissionReviewMapper.selectByPrimaryKey(Integer.parseInt(reviewId));
                if (null == review) {
                    LOG.info(String.format("佣金审核放款,未查询到reviewId【%s】的审核单详情。。。", reviewId));
                    return false;
                }

                if (CommissionReviewStatusEnum.AGREE_REVIEW.getStatus() != review.getStatus()) {
                    LOG.info(String.format("佣金审核放款,reviewId【%s】 status【%s】,拒绝放款。", reviewId, review.getStatus()));
                    return false;
                }

                BankVo bankVo = bankInfoMapper.selectMainBankByUserId(Integer.valueOf(review.getPerId()));
                LOG.info(String.format("-----------> query bank info param【%s】, result【%s】", review.getPerId(), bankVo));

                WithdrawalVo withdrawalVo = new WithdrawalVo();
                withdrawalVo.setPerId(Integer.parseInt(review.getPerId()));
                withdrawalVo.setAmount(Float.parseFloat(review.getApplyAmount().toString()));
                withdrawalVo.setBankNum(bankVo.getBankNum());
                withdrawalVo.setPayChannel("");
                withdrawalVo.setTriggerStyle(PayTriggerStyleEnum.BACK_GROUND.getCode());
                withdrawalVo.setContractId(review.getId());

                // 调用dubbo服务，佣金提现
                LOG.info(String.format("-----------> 调用放款dubbo param【%s】", withdrawalVo));
                ResponseDo<Integer> response = withdrawalService.getCommissionWithdrawal(withdrawalVo);
                LOG.info(String.format("-----------> 调用放款dubbo result【%s】", response));

                // 更新用户佣金获取流水表(4 放款中 2 待放款)
                commissionOrderMapper.updateWithDrawalStatus(response.isSuccess() ? CommissionOrderStatusEnum.LOANING.getStatusString() : CommissionOrderStatusEnum.PENDING.getStatusString(), Arrays.asList(review.getCommissionOrderIds().split(",")));

                // 更新用户佣金领取审核表(1 放款中 3 放款失败)
                review.setStatus(response.isSuccess() ? CommissionReviewStatusEnum.AGREE_REVIEW.getStatus() : CommissionReviewStatusEnum.AGREE_BUT_PAY_FAIL.getStatus());
                if (response.isSuccess()) {
                    review.setLoanOrderId(response.getData());
                }
                commissionReviewMapper.updateByPrimaryKey(review);

                return true;
            } catch (Exception e) {
                LOG.error("Exception happens when commission loan ", e);
                return false;
            }
        }

        @Override
        public String toString() {
            return reviewId;
        }

    }

    private static void handleCondition(Map<String, Object> conditionMap, Example.Criteria criteria) {
        for (String entry : conditionMap.keySet()) {
            try {
                if (entry.contains("_start")) {
                    String key = entry.substring(0, entry.indexOf("_"));
                    criteria.andGreaterThan(key, DateUtil.stampToDate((String) conditionMap.get(entry)));
                    continue;
                }
                if (entry.contains("_end")) {
                    String key = entry.substring(0, entry.indexOf("_"));
                    criteria.andLessThan(key, DateUtil.stampToDate((String) conditionMap.get(entry)));
                    continue;
                }
                if (null == conditionMap.get(entry + "_start")) {
                    criteria.andEqualTo(entry, conditionMap.get(entry));
                }
            } catch (MapperException e) {
                LOG.error(e);
            }
        }
    }

}