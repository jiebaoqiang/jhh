package com.jhh.dc.baika.manage.controller.report;

import com.github.pagehelper.PageHelper;
import com.jhh.dc.baika.manage.controller.BaseController;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.service.report.ReportService;
import com.jhh.dc.baika.manage.service.user.UserService;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.entity.manager_vo.PhoneBookVo;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jhh.dc.baika.common.util.ExcelUtils;

import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanzezhong on 2018/1/2.
 */
@Controller
public class ReportController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);


    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    /**
     * 获取聚信立报告
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/report/polyXinli", method = RequestMethod.GET)
    public Response getRiskReport() {
        return reportService.getPolyXinliReport();
    }

    /**
     * 手机通讯录联系人
     */
    @ResponseBody
    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public Response getContact(HttpServletRequest request,
                               @RequestParam Integer perId, @RequestParam String phones) throws IOException {
        QueryParamUtils.buildPage(request);
        int offset = Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
        int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));

        return reportService.getContact(perId, phones, offset, size);
//         JSON.toJSONString(new PageInfo(result));
    }

    /**
     * 个人通讯录导出
     *
     * @param request
     * @param response
     * @param perId
     * @return
     */
    @RequestMapping("/export/phonebook")
    public void exportPhoneBook(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer perId) {
        try {
//            List<PhoneBookVo>
            Response result = reportService.getContactForExport(perId);
            if (result.getData() != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(NormalExcelConstants.FILE_NAME, userService.getNameByPersonId(perId) + "的通信录" + DateUtil.getDateStringToHHmmss(new Date()).replace(" ", "-"));
                map.put(NormalExcelConstants.CLASS, PhoneBookVo.class);
                map.put(NormalExcelConstants.DATA_LIST, result.getData());
                map.put(NormalExcelConstants.PARAMS, new ExportParams());
                ExcelUtils.jeecgSingleExcel(map, request, response);
            } else {
                logger.warn("通讯录为空，perId=" + perId);
            }
        } catch (Exception e) {
            logger.error("无法获取通讯录", e);
        }
    }
}
