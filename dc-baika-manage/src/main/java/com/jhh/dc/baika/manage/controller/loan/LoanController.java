package com.jhh.dc.baika.manage.controller.loan;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.api.loan.RepaymentPlanService;
import com.jhh.dc.baika.entity.common.ResponseCode;
import com.jhh.dc.baika.entity.manager_vo.PrivateVo;
import com.jhh.dc.baika.manage.controller.BaseController;
import com.jhh.dc.baika.manage.entity.PolyXinLi;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.entity.Result;
import com.jhh.dc.baika.manage.service.loan.LoanService;
import com.jhh.dc.baika.manage.service.user.UserService;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.common.util.Detect;
import com.jhh.dc.baika.common.util.ExcelUtils;
import com.jhh.dc.baika.common.util.PropertiesReaderUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenchao
 */
@Controller
public class LoanController extends BaseController{

    private static final Logger logger = org.apache.log4j.LogManager.getLogger(LoanController.class);

    @Autowired
    private RepaymentPlanService repaymentPlanService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoanService loanService;

    /**
     * 悠兔白卡后台查看交易流水
     *
     * @param request
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = "/repayTermPlan", produces = "application/json")
    public Result getRepaymentTermPlan(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Result result = new Result();
        List info = repaymentPlanService.getRepaymentTermPlan(request.getParameter("borrId"));
        result.setCode(Result.SUCCESS);
        result.setMessage("加载成功");
        result.setObject(new PageInfo(info));
        return result;
    }

    /**
     * 悠兔白卡后台查看基本信息
     *
     * @param perId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public PrivateVo getUserInfo(@RequestParam int perId, HttpServletResponse response) {
        Response result = userService.getUserInfo(perId);
        if (result.getCode() == ResponseCode.SUCCESS) {
            return (PrivateVo) result.getData();
        }
        return new PrivateVo();
    }

    /**
     * 聚信立信用报告列表
     */
    @ResponseBody
    @RequestMapping(value = "polyXinli/credit", method = RequestMethod.GET)
    public String getjuxinliCredit(HttpServletRequest request, HttpServletResponse response
            , @RequestParam Integer perId) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));

        List result = loanService.getPolyXinliCredit(perId, offset, size);

        return JSON.toJSONString(new PageInfo(result));

    }

    /**
     * 聚信立报告导出
     *
     * @param request
     * @param response
     * @param perId
     * @return
     */
    @RequestMapping("/export/polyXinliCredit")
    public void exportPolyXinliCredit(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer perId, @RequestParam String phone, @RequestParam String name) {
        try {
            List result = loanService.getPolyXinliCredit(perId, 0, Integer.MAX_VALUE);
            if (result != null || result.isEmpty()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(NormalExcelConstants.FILE_NAME, name + phone + "聚信立报告");
                map.put(NormalExcelConstants.CLASS, PolyXinLi.class);
                List dataList = ((Page) result).getResult();
                if (dataList == null) {
                    dataList = Collections.EMPTY_LIST;
                }
                map.put(NormalExcelConstants.DATA_LIST, dataList.get(0));
                map.put(NormalExcelConstants.PARAMS, new ExportParams());
                ExcelUtils.jeecgSingleExcel(map, request, response);
            } else {
                logger.warn("通讯记录为空，perId=" + perId);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * 手机通讯录联系人
     */
    @ResponseBody
    @RequestMapping(value = "polyXinli/contact", method = RequestMethod.GET)
    public String getContact(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam Integer perId) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
        List result = loanService.getContact(perId, offset, size);
        return JSON.toJSONString(new PageInfo(result));
    }

    /**
     * 手机通讯录联系人
     */
    @ResponseBody
    @RequestMapping(value = "polyXinli/creditInvestigation", method = RequestMethod.GET)
    public String getCreditInvestigation(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam String phone, @RequestParam String idcard) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String jxlUrl = PropertiesReaderUtil.read("system", "RC_CIS_URL");
        return jxlUrl + "?phone=" + phone + "&idcard=" + idcard;
    }


    /**
     * 悠米后台查看催收备注信息
     *
     * @param request
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = "/memo", method = RequestMethod.GET)
    public String getMemos(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer borrId) throws UnsupportedEncodingException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        QueryParamUtils.buildPage(request);
        List result = loanService.getMemos(request.getParameterMap(), borrId);
        return JSON.toJSONString(new PageInfo(result));
    }

    /**
     * 统计报告列表
     */
    @ResponseBody
    @RequestMapping(value = "/workReport", method = RequestMethod.POST)
    public String getWorkReport(HttpServletRequest request,
                                HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> map = new HashMap<String, Object>();
        String bedueName = request.getParameter("bedueName");
        String levelType = request.getParameter("levelType");
        if (Detect.notEmpty(bedueName)) {
            map.put("bedueName", bedueName);
        }
        if (StringUtils.isNotBlank(levelType) && StringUtils.isNumeric(levelType)) {
            map.put("levelType", levelType);
        }
        map.put("beginDate", request.getParameter("beginDate"));
        map.put("endDate", request.getParameter("endDate"));
        return JSON.toJSONString(new PageInfo(loanService.workReport(map)));
    }

    /**
     * 流水列表
     * @param borrId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/loan/order", method = RequestMethod.GET)
    public String getOrderList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer borrId) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        QueryParamUtils.buildPage(request);
        List result = loanService.getOrderList(request.getParameterMap(), borrId);
        return JSON.toJSONString(new PageInfo(result));
    }

    /**
     * 单独查询订单状态接口
     * @param serialNo 订单号
     * @return
     */
    @ResponseBody
    @RequestMapping("/orderStatus")
    public Response orderStatus(String serialNo) throws Exception {

        return loanService.getOrderState(serialNo);
    }
}
