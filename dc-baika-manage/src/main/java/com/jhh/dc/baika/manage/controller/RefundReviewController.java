package com.jhh.dc.baika.manage.controller;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.entity.refund.RefundReviewVo;
import com.jhh.dc.baika.manage.service.refund.RefundReviewService;
import com.jhh.dc.baika.common.util.CodeReturn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("refundReview")
@Slf4j
public class RefundReviewController extends BaseController{
    @Autowired
   private RefundReviewService refundReviewService;


    /**
     * 获取退款列表
     * @return
     */
    @RequestMapping(value = "queryRefundReviewList")
    public @ResponseBody String queryRefundReviewList(HttpServletRequest request) {
        int offset =  Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
        String auth=request.getParameter("auth");
        try {
            PageInfo<RefundReviewVo> refundReviewPageInfo = refundReviewService.queryRefundReviewList(request,offset,size,auth);
            return JSON.toJSONString(refundReviewPageInfo);
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(new PageInfo<RefundReviewVo>());
        }
    }

    /**
     * 退款确认
     * @param perId 用户Id
     * @param operator 操作人
     * @return
     */
    @RequestMapping(value = "/affirm/{perId}")
    @ResponseBody
    Response affirmRefund(@PathVariable Integer perId,
                          @RequestParam(required = false) String operator){
        return refundReviewService.affirmRefund(perId, operator);
    }


    /**
     * 退款
     * @param refundId 退款审核id
     * @param remark 备注
     * @param operator 操作人
     * @return
     */
    @RequestMapping(value = "/refund/{refundId}")
    @ResponseBody
    Response refund(@PathVariable Integer refundId,
                    @RequestParam(required = false) String remark,
                    @RequestParam(required = false) String operator){
        return refundReviewService.refund(refundId,remark, operator);
    }

    /**
     * 退款拒绝
     * @param refundId 退款审核id
     * @param remark 备注
     * @return
     */
    @RequestMapping(value = "/reject/{refundId}")
    @ResponseBody
    Response rejectRefund(@PathVariable Integer refundId,
                          @RequestParam(required = false)  String remark,
                          @RequestParam(required = false) String operator){
        return refundReviewService.rejectRefund(refundId,remark,operator);
    }

    /**
     * 接收定时任务的回调<br />
     * 发送站内短信
     */
    @RequestMapping("/callback")
    @ResponseBody
    public Response callback(String perId,String status,String serialNo){
        if(StringUtils.isEmpty(perId) || StringUtils.isEmpty(status) || StringUtils.isEmpty(serialNo)){
            log.warn("退款回调请求参数异常，perId = {},status = {}, serialNo = {}", perId, status, serialNo);

            return new Response().code(CodeReturn.fail).msg("参数异常");
        }
        return refundReviewService.callback(perId,status,serialNo);
    }
}
