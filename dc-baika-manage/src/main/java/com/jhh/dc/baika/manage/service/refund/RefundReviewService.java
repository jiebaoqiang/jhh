package com.jhh.dc.baika.manage.service.refund;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.entity.refund.RefundReviewVo;

import javax.servlet.http.HttpServletRequest;

public interface RefundReviewService {

    /**
     * 退款列表
     * @param
     * @returnF
     */
    PageInfo<RefundReviewVo> queryRefundReviewList(HttpServletRequest request,int offset, int size,String auth);

    /**
     * 退款确认
     * @param perId 用户Id
     * @param operator 操作人
     * @return
     */
    Response affirmRefund(Integer perId, String operator);

    /**
     * 退款
     * @param refundId 退款审核id
     * @param remark 备注
     * @param operator 操作人
     * @return
     */
    Response refund(Integer refundId, String remark, String operator);

    /**
     * 退款拒绝
     * @param refundId 退款审核id
     * @param remark 备注
     * @param operator 操作人
     * @return
     */
    Response rejectRefund(Integer refundId, String remark,String operator);

    /**
     * 回调发送短信
     */
    Response callback(String perId,String status,String serialNo);
}
