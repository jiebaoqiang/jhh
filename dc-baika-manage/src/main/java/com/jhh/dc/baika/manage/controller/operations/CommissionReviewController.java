package com.jhh.dc.baika.manage.controller.operations;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.entity.app.NoteResult;
import com.jhh.dc.baika.entity.manager_vo.CommissionDetailVo;
import com.jhh.dc.baika.entity.share.CommissionReview;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.entity.SimpleResp;
import com.jhh.dc.baika.manage.service.Commission.CommissionReviewService;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.common.util.ExcelUtils;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xingmin
 */
@Controller
@RequestMapping("commissionReview")
public class CommissionReviewController {

    @Autowired
    private CommissionReviewService commissionReviewService;


    /**
     * 获取佣金审核列表
     * @return
     */
    @RequestMapping(value = "queryCommissionReviewList")
    public @ResponseBody
    String queryCommissionReviewList(HttpServletRequest request) {
        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
        try {
            Map<String, Object> queryMap = QueryParamUtils.getargs(request.getParameterMap());

            PageInfo<CommissionReview> commissionReviewPageInfo = commissionReviewService.queryCommissionReviewList(queryMap, offset, size, false);
            return JSON.toJSONString(commissionReviewPageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new PageInfo<CommissionReview>());
        }
    }


    /**
     * 获取佣金审核结果列表
     * @return
     */
    @RequestMapping(value = "queryCommissionReviewResultList")
    public @ResponseBody
    String queryCommissionReviewResultList(HttpServletRequest request) {
        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
        try {
            Map<String, Object> queryMap = QueryParamUtils.getargs(request.getParameterMap());

            PageInfo<CommissionReview> commissionReviewPageInfo = commissionReviewService.queryCommissionReviewList(queryMap, offset, size, true);
            return JSON.toJSONString(commissionReviewPageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new PageInfo<CommissionReview>());
        }
    }

    /**
     * 佣金审核
     */
    @ResponseBody
    @RequestMapping(value = "/updateCommissionReviewResult", produces = "application/json", method = RequestMethod.POST)
    public Response updateCommissionReviewResult(HttpServletRequest request) {
        String reviewId = request.getParameter("reviewId");
        if (StringUtils.isEmpty(reviewId)) {
            return new Response().getResponse(-100, "请选择要审核的记录!", "");
        }

        String reviewResult = request.getParameter("reviewResult");
        if (StringUtils.isEmpty(reviewResult)) {
            return new Response().getResponse(-100, "审核结果不能为空!", "");
        }


        String reviewReason = request.getParameter("reviewReason");

        String userId=request.getParameter("userId");
        try {
            return commissionReviewService.updateCommissionReviewResult(reviewId, reviewResult, reviewReason,userId);
        }catch (Exception e) {
            return new Response().getResponse(-100, String.format("系统异常【%s】", e.getMessage()), "");
        }
    }

    /**
     * 放款成功通知
     * @param reviewId 审核单ID
     * @param status   放款结果（1 成功 0 失败）
     */
    @ResponseBody
    @RequestMapping(value = "/callback", produces = "application/json", method = RequestMethod.POST)
    public NoteResult payCallback(HttpServletRequest request) {
        String reviewId = request.getParameter("reviewId");
        if (StringUtils.isEmpty(reviewId)) {
            return NoteResult.FAIL_RESPONSE(String.format("参数有误 reviewId【%s】", reviewId));
        }

        String status = request.getParameter("status");
        if (StringUtils.isEmpty(reviewId)) {
            return NoteResult.FAIL_RESPONSE(String.format("参数有误 status【%s】", status));
        }

        try {
            return commissionReviewService.updateCommissionPayResult(reviewId, status);
        }catch (Exception e) {
            return NoteResult.FAIL_RESPONSE(String.format("系统异常【%s】", e.getMessage()));
        }
    }

    /**
     * 佣金审核详情
     * @param perId
     * @param limit
     * @param offset
     * @param level
     * @param phone
     * @return
     */
    @RequestMapping("/detail/{perId}")
    @ResponseBody
    public SimpleResp<List<CommissionDetailVo>> queryCommissionReviewDetail(@PathVariable("perId")String perId,String perId2,int limit,int offset,String level,String phone){
        return commissionReviewService.queryCommissionReviewDetail(perId,perId2,offset,limit,level,phone);
    }

    /**
     * 导出佣金审核详情
     * @param request
     * @return
     */
    @PostMapping(value = "/exportCommissionDetail")
    @ResponseBody
    public void exportCommissionDetail(HttpServletRequest request, HttpServletResponse response){
        String personId = request.getParameter("personId");
        List<CommissionDetailVo> result = commissionReviewService.commissionDetailByPersonId(personId);

        Map<String, Object> map = new HashMap();
        map.put(NormalExcelConstants.FILE_NAME, "佣金审核详情" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        map.put(NormalExcelConstants.CLASS, CommissionDetailVo.class);
        map.put(NormalExcelConstants.DATA_LIST, result);
        map.put(NormalExcelConstants.PARAMS, new ExportParams());
        ExcelUtils.jeecgSingleExcel(map, request,response);
    }

}
