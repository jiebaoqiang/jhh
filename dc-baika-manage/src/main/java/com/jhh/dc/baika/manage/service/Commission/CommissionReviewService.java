package com.jhh.dc.baika.manage.service.Commission;

import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.manager_vo.CommissionDetailVo;
import com.jhh.dc.baika.entity.share.CommissionReview;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.entity.SimpleResp;

import java.util.List;
import java.util.Map;

/**
 * @author xingmin
 */
public interface CommissionReviewService {

    /**
     * 获取审核结果列表
     * @return PageInfo<CommissionReview>
     */
    PageInfo<CommissionReview> queryCommissionReviewList(Map<String, Object> queryMap, int offset, int size, boolean isReview);

    /**
     * 佣金审核放款
     * @param reviewId      佣金审核单ID
     * @param reviewResult  审核结果（1 通过/ 0 拒绝）
     * @param reviewReason  审核理由
     * @return Response
     */
    Response updateCommissionReviewResult(String reviewId, String reviewResult, String reviewReason,String userId);

    /**
     * 放款成功回调
     * @param reviewId 审核单ID
     * @param status   放款结果（1 成功 0 失败）
     * @return
     */
    NoteResult updateCommissionPayResult(String reviewId, String status);

    /**
     * 审核详情
     */
    SimpleResp<List<CommissionDetailVo>> queryCommissionReviewDetail(String perId,String perId2, int offset, int size,String level,String phone);

    /**
     * 根据用户id获取佣金审核详情
     * @param personId
     * @return
     */
    List<CommissionDetailVo> commissionDetailByPersonId(String personId);

}
