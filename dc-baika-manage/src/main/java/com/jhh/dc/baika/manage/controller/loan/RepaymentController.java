package com.jhh.dc.baika.manage.controller.loan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.api.manager.ManageInfoService;
import com.jhh.dc.baika.entity.loan.PayChannelAdapter;
import com.jhh.dc.baika.entity.manager.CodeValue;
import com.jhh.dc.baika.manage.entity.DownloadOrder;
import com.jhh.dc.baika.manage.mapper.PayChannelAdapterMapper;
import com.jhh.dc.baika.manage.service.loan.RepaymentService;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.common.util.ExcelUtils;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//贷后管理
@Controller
@RequestMapping("/repayment")
public class RepaymentController {

    @Autowired
    private ManageInfoService manageInfoService;

    @Autowired
    private RepaymentService repaymentService;

    @Autowired
    private PayChannelAdapterMapper payChannelMapper;

    /**
     * 还款计划
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/plan", method = RequestMethod.GET)
    public String getRepaymentPlan(HttpServletRequest request) {
        QueryParamUtils.buildPage(request);
        List result = repaymentService.getRepaymentPlan(request.getParameterMap());
        return JSON.toJSONString(new PageInfo(result));
    }

    /**
     * 还款流水
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String getRepaymentOrder(HttpServletRequest request) {
        PageInfo result = repaymentService.getRepaymentOrder(request);
        return JSON.toJSONString(result);
    }

    /**
     * 导出还款流水
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order/export")
    public void exportRepaymentOrder(HttpServletRequest request, HttpServletResponse response) {

        List<CodeValue> orderTypes = manageInfoService.getCodeValueListByCode("order_type");
        List<CodeValue> payCenterChannels = manageInfoService.getCodeValueListByCode("pay_center_channel");

        JSONObject orderTypeObject = new JSONObject(true);

        orderTypeObject.put("6", "减免");
        orderTypeObject.put("7", "线下还款");

        orderTypeObject.put("1", "放款(银生宝)");
        orderTypeObject.put("5", "主动还款(银生宝)");
        orderTypeObject.put("4", "还款(代收)(银生宝)");
        orderTypeObject.put("8", "批量代扣(银生宝)");
        orderTypeObject.put("11", "放款(海尔)");
        orderTypeObject.put("12", "主动还款(海尔)");
        orderTypeObject.put("13", "还款(代收)(海尔)");
        orderTypeObject.put("14", "批量代扣(海尔)");


        List<PayChannelAdapter> channels = payChannelMapper.selectAll();
        for (int i = 0; i < channels.size(); i++) {
            PayChannelAdapter adapter = channels.get(i);
            if ("15".equals(adapter.getType())) {
                orderTypeObject.put(adapter.getType() + "/" + adapter.getChannel(), "放款(" + adapter.getName() + ")");
            }
            if ("16".equals(adapter.getType())) {
                orderTypeObject.put(adapter.getType() + "/" + adapter.getChannel(), "主动还款(" + adapter.getName() + ")");
            }
            if ("17".equals(adapter.getType())) {
                orderTypeObject.put(adapter.getType() + "/" + adapter.getChannel(), "还款(代收)(" + adapter.getName() + ")");
            }
            if ("18".equals(adapter.getType())) {
                orderTypeObject.put(adapter.getType() + "/" + adapter.getChannel(), "批量代扣(" + adapter.getName() + ")");
            }
        }

        for (int j = 0; j < orderTypes.size(); j++) {
            CodeValue orderType = orderTypes.get(j);
            Integer orderTypeValue = Integer.valueOf(orderType.getCodeCode());
            if (orderTypeValue < 15 || orderTypeValue > 18) {
                continue;
            }

            if (!orderTypeObject.containsKey(orderType.getCodeCode())) {
                orderTypeObject.put(orderType.getCodeCode(), orderType.getMeaning() + "(支付中心)");
            }
        }

        PageInfo result = repaymentService.getRepaymentOrder(request, 100000);
        Map<String, Object> map = new HashMap();
        map.put(NormalExcelConstants.FILE_NAME, "还款流水" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
        map.put(NormalExcelConstants.CLASS, DownloadOrder.class);
        List<DownloadOrder> orders = JSON.parseArray(JSON.toJSONString(result.getList()), DownloadOrder.class);
        orders.forEach(t -> {
            int type = Integer.parseInt(t.getType());
            if (type < 15 || type > 18) {
                t.setSidNo(t.getSerialNo());
            } else {
                t.setTypeName(orderTypeObject.getString(t.getTypeWithChannel()));
            }
        });
        map.put(NormalExcelConstants.DATA_LIST, orders);
        map.put(NormalExcelConstants.PARAMS, new ExportParams());
        ExcelUtils.jeecgSingleExcel(map, request, response);
    }

    /**
     * 查询催收队列
     */
    @RequestMapping(value = "/queryCollectors", method = RequestMethod.GET)
    @ResponseBody
    public String queryCollectors(HttpServletRequest request, @RequestParam String userId) {
        QueryParamUtils.buildPage(request);
        List result = repaymentService.selectCollectorsList(request.getParameterMap(), userId);
        return JSON.toJSONString(new PageInfo(result));

    }
}
