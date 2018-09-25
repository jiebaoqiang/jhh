package com.jhh.dc.baika.manage.controller.operations;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.api.manager.ManageInfoService;
import com.jhh.dc.baika.entity.manager.CodeValue;
import com.jhh.dc.baika.entity.share.CommissionRule;
import com.jhh.dc.baika.entity.utils.ManagerResult;
import com.jhh.dc.baika.manage.service.Commission.CommissionRuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("commissionRule")
public class CommissionRuleController {
    @Autowired
   private CommissionRuleService commissionRuleService;

    @Autowired
    private ManageInfoService manageInfoService;

    /**
     * 获取规则列表
     * @return
     */
    @RequestMapping(value = "queryCommissionRuleList")
    public @ResponseBody String queryCommissionRuleList(HttpServletRequest request) {
        int offset =  Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
        try {
            Map<String,Object> queryMap = QueryParamUtils.getargs(request.getParameterMap());
            PageInfo<CommissionRule> commissionRulePageInfo = commissionRuleService.queryCommissionRuleList(queryMap,offset,size);
            return JSON.toJSONString(commissionRulePageInfo);
        }catch (Exception e){
            e.printStackTrace();
            return JSON.toJSONString(new PageInfo<CommissionRule>());
        }
    }

    /**
     * 删除规则列表根据id
     * @param idfordel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteCommissionRule", method = RequestMethod.POST)
    public ManagerResult deleteCommissionRule(String idfordel) {

        return commissionRuleService.deleteCommissionRule(idfordel);
    }

    /**
     * 新增规则信息
     * @param commissionRule
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertCommissionRule", method = RequestMethod.POST)
    public ManagerResult insertCodeType(CommissionRule commissionRule) {

        return commissionRuleService.insertCommissionRule(commissionRule);
    }

    /**
     * 修改规则信息
     * @param commissionRule
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateCommissionRule", method = RequestMethod.POST)
    public ManagerResult updateCommissionRule(CommissionRule commissionRule) {

        return commissionRuleService.updateCommissionRule(commissionRule);
    }


    @ResponseBody
    @RequestMapping(value = "/queryCommRuleGroup")
    public   List<CodeValue>  queryCommRuleGroup() {
        return manageInfoService.getCodeValueListByCode("commission_rule_group");
    }

    @ResponseBody
    @RequestMapping(value = "/queryCommRuleInviterLevel")
    public   List<CodeValue>  queryCommRuleInviterLevel() {
        return manageInfoService.getCodeValueListByCode("commission_rule_inviter_level");
    }
    @ResponseBody
    @RequestMapping(value = "/queryCommRuleTrackStatus")
    public   List<CodeValue> queryCommRuleTrackStatus() {
        return manageInfoService.getCodeValueListByCode("commission_rule_tracking_status");
    }
}
