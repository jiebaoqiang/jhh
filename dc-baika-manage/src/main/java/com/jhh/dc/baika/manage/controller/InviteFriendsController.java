package com.jhh.dc.baika.manage.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.entity.manager_vo.CommissionDetailVo;
import com.jhh.dc.baika.entity.manager_vo.CommissionReceiveVo;
import com.jhh.dc.baika.entity.share.CommissionSummary;
import com.jhh.dc.baika.manage.service.Commission.InviteFriendsService;
import com.jhh.dc.baika.common.util.ExcelUtils;

import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("friends")
public class InviteFriendsController {

    @Autowired
    private InviteFriendsService inviteFriendsService;


    /**
     * 获取邀请好友列表
     * @return
     */
    @RequestMapping(value = "inviteFriends")
    public @ResponseBody
    String queryInviteFriendsList(HttpServletRequest request) {
        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
        try {
            Map<String, Object> queryMap = QueryParamUtils.getargs(request.getParameterMap());
            PageInfo<CommissionSummary> commissionReviewPageInfo = inviteFriendsService.queryCommissionSummaryList(queryMap, offset, size, false);
            return JSON.toJSONString(commissionReviewPageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new PageInfo<CommissionSummary>());
        }
    }


    /**
     * 导出获取邀请好友列表
     * @return
     */
    @RequestMapping(value = "exportInviteFriendsList")
    public void exportInviteFriendsList(HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> queryMap = QueryParamUtils.getargs(request.getParameterMap());
        PageInfo<CommissionSummary> result = inviteFriendsService.exportCommissionSummaryList(queryMap);
        Map<String, Object> map = new HashMap();
        map.put(NormalExcelConstants.FILE_NAME, "好友邀请列表" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        map.put(NormalExcelConstants.CLASS, CommissionSummary.class);
        map.put(NormalExcelConstants.DATA_LIST, result.getList());
        map.put(NormalExcelConstants.PARAMS, new ExportParams());
        ExcelUtils.jeecgSingleExcel(map, request,response);
    }

    @RequestMapping("/queryInviterDetail/{perId}")
    @ResponseBody
    public String queryInviterDetail(HttpServletRequest request, @PathVariable("perId") String perId){

        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
        Map<String,Object> queryMap = QueryParamUtils.getargs(request.getParameterMap());
        PageInfo<CommissionReceiveVo> result = inviteFriendsService.queryInviterDetail(queryMap,perId,offset,size);

        return JSON.toJSONString(result);
    }

    /**
     * 导出佣金领取记录
     * @param request
     * @return
     */
    @PostMapping(value = "/exportCommissionGetHistory")
    public void exportCommissionGetHistory(HttpServletRequest request, HttpServletResponse response){
        String personId = request.getParameter("personId");
        List<CommissionReceiveVo> result = inviteFriendsService.exportCommissionGetHistory(personId);

        Map<String, Object> map = new HashMap();
        map.put(NormalExcelConstants.FILE_NAME, "佣金领取记录" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        map.put(NormalExcelConstants.CLASS, CommissionReceiveVo.class);
        map.put(NormalExcelConstants.DATA_LIST, result);
        map.put(NormalExcelConstants.PARAMS, new ExportParams());
        ExcelUtils.jeecgSingleExcel(map, request,response);
    }
}
