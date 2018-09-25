package com.jhh.dc.baika.service.impl;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jhh.dc.baika.dao.OrderRobotMapper;
import com.jhh.dc.baika.service.RobotService;
import com.jhh.dc.baika.util.*;
import com.jhh.dc.baika.dao.BorrowListMapper;
import com.jhh.dc.baika.dao.CardMapper;
import com.jhh.dc.baika.entity.app.BorrowList;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.model.OrderRobot;
import com.jhh.dc.baika.model.PromptData;
import com.jhh.dc.baika.model.Response;
import com.jhh.dc.baika.service.MailService;
import com.jhh.dc.baika.util.*;
import com.jhh.dc.baika.common.util.http.AOSHttpClient;
import com.jhh.dc.baika.common.util.http.HttpRequestVO;
import com.jhh.dc.baika.common.util.http.HttpResponseVO;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Log4j
@Component
public class RobotServiceImpl implements RobotService {

    private static final Logger logger = LoggerFactory.getLogger(RobotServiceImpl.class);

    private static String corpCode = "悠回收";

    @Value("${system.isTest}")
    private String isTest;

    //百可录发送路径，用户名，地址
    @Value("${mail.wzz.url}")
    private String url;
    @Value("${mail.wzz.client_name}")
    private String client_name;
    @Value("${mail.wzz.access_token}")
    private String access_token;
    //本地excel文件生成路径
    @Value("${mail.wzz.promptPath}")
    private String promptPath;

    @Value("${ROBOT_ORDER_URl}")
    private String robotUrl;
    @Value("${ROBOT_ORDER_ACCESS_TOKEN}")
    private String accessToken;
    @Value("${ROBOT_ORDER_JOB_CODE}")
    private String jobCode;
    @Value("${ROBOT_FILE_URL}")
    private String robotFileUrl;

    @Autowired
    OrderRobotMapper orderRobotMapper;

    @Autowired
    CardMapper cardMapper;

    @Autowired
    private BorrowListMapper borrowListMapper;

    @Autowired
    private MailService mailService;

    @Override
    public void sendDataToBaikelu() {
        log.info("进入定时发送机器人数据");
        try {
            List<PromptData> promptData = borrowListMapper.getPromptData();
            int promptSize = 0;
            if (promptData != null && promptData.size() > 0) {
                promptSize = promptData.size();
            }
            log.info("共有数据" + promptSize);
            // 生成Excel
            String fileName = ExcelUtils.createExcel(promptData, promptPath, "YHS-P0-" + DateUtil.getDateString(new Date()));
            File file = new File(promptPath + fileName);
            //百科录正式环境地址，修改成调用接口发送
            //如果当天数据为空，则不发送excel
            if (promptSize > 0) {
                //百可录发送参数
                Map<String, String> param = new HashMap<>();
                param.put("file", "file");
                param.put("client_name", client_name);
                param.put("access_token", access_token);
                log.info("url:" + url + ";param:" + param + ";file:" + file.getAbsolutePath());
                HttpUtils.postToFile(url, param, file);
            }
            file.delete();
        } catch (Exception e) {
            String[] to = {"chailongjie@biocloo.com.cn", "m0_support@biocloo.com.cn", "xuyaozong@jinhuhang.com.cn", "yangyan@jinhuhang.com.cn"};
            String[] copyto = {"luoqian@jinhuhang.com.cn", "dinghaifeng@jinhuhang.com.cn", "liuhongming@jinhuhang.com.cn", "chenzhen@jinhuhang.com.cn", "wanzezhong@jinhuhang.com.cn", "mengqingkun@jinhuhang.com.cn"};
            if ("on".equals(isTest)) {
                to = new String[]{"mengqingkun@jinhuhang.com.cn"};
                copyto = new String[]{"mengqingkun@jinhuhang.com.cn"};
            }
            mailService.sendMail(to, copyto, "DC_百可录逾期提醒异常", e.getMessage());
            log.error(e);
        }
    }

    public Response sendRcOrder(BorrowList borrowList) {
        Response response = new Response();
        if (borrowList != null) {
            String code = UUID.randomUUID() + "";
            //给第三方发送数据
            HttpResponseVO httpResponseVO = sendPostRobot(code, borrowList);

            if (httpResponseVO != null && httpResponseVO.getStatus().equals("200")) {
                JSONObject json = JSONObject.parseObject(httpResponseVO.getOut());

                if (Detect.notEmpty(json.getString("success"))) {
                    JSONObject jsonSucess = JSONObject.parseObject(json.getString("success"));

                    OrderRobot orderRobot = new OrderRobot();
                    orderRobot.setCode(code);
                    orderRobot.setBorrId(borrowList.getId());
                    orderRobot.setState(Constants.OrderRobotState.SEND_ING);
                    orderRobot.setThirdCode(jsonSucess.getString("receipt_id"));
                    orderRobot.setSendParam(httpResponseVO.getSendParam());
                    orderRobot.setRecvParam(httpResponseVO.getOut());
                    orderRobot.setCreateDate(Calendar.getInstance().getTime());
                    orderRobot.setUpdateDate(Calendar.getInstance().getTime());
                    orderRobot.setSucessTime(jsonSucess.getString("received_time"));
                    orderRobotMapper.insertSelective(orderRobot);

                    //更新boor电呼状态
                    BorrowList bl = new BorrowList();
                    bl.setId(orderRobot.getBorrId());
                    bl.setBaikeluStatus(orderRobot.getState());
                    borrowListMapper.updateByPrimaryKeySelective(bl);

                    response.setCode(200);
                    response.setMsg("success");
                } else {
                    response.setData(json.getString("failure"));
                }
                logger.info("sendRcOrder backParam:" + httpResponseVO.getOut());
            }
        }
        return response;
    }

    @Override
    public Response sendRcOrder(Integer borrId) {
        if (!Detect.isPositive(borrId)) {
            logger.error("合同Id不能为空");
            return Response.failValue("合同Id不能为空");
        }
        Response response = new Response();
        response.setCode(5000);
        BorrowList borrowList = borrowListMapper.selectByPrimaryKey(borrId);
        if (borrowList == null) {
            String errorMessage = "合同不存在, 合同id:" + borrId;
            logger.error(errorMessage);
            return Response.failValue(errorMessage);
        }
        if (!borrowList.getBorrStatus().equals("BS003")) {
            String errorMessage = "合同状态必须为已签约，合同id: " + borrId;
            logger.error(errorMessage);
            return Response.failValue(errorMessage);
        }
        //有放过款的不打电话,正常结清和逾期结清和提前结清
        List<BorrowList> borrowLists = borrowListMapper.queryBorrListByPerIdAndStauts(borrowList.getPerId());
        if (Detect.notEmpty(borrowLists)) {
            String errorMessage = "已放过款，不再打电话，合同id: " + borrId;
            logger.error(errorMessage);
            return Response.failValue(errorMessage);
        }
        //合同状态必须为已签约
        response = sendRcOrder(borrowList);
        return response;

    }

    private HttpResponseVO sendPostRobot(String code, BorrowList borrowList) {
        Map map = new HashMap();
        map.put("work_id", code);
        map.put("corp_code", corpCode);
        map.put("access_token", accessToken);
        map.put("job_code", jobCode);
        //构造问题答案数据
        String work_data = getWorkData(borrowList.getId());
        Map dtt = null;
        try {
            dtt = JSONObject.parseObject(work_data, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> mdata = Maps.newHashMap();
        map.put("work_data", dtt);
        mdata.put("mdata", JSONObject.toJSONString(map));
        HttpRequestVO httpRequestVO = new HttpRequestVO(robotUrl, mdata);
        HttpResponseVO httpResponseVO = AOSHttpClient.upload(httpRequestVO);
        httpResponseVO.setSendParam(JSONObject.toJSONString(mdata));
        return httpResponseVO;
    }

    /**
     * csv_bank_digit_4   银行卡后四位
     * csv_phone_num 电话号码
     * csv_dob_yyyymmdd 出生年月日
     * csv_digit_4  身份证后四位
     * csv_borrow_amt 借款金额
     * csv_borrow_period 分期天数
     *
     * @return
     */

    private String getWorkData(Integer borrowId) {
        //Assertion.isPositive(borrowId,"合同ID不能为空");
        Assertion.isPositive(borrowId, "获取审核电话问题答案时，传入合同id为空");
        //查询百科录问题信息
        Map<String, Object> map = cardMapper.queryRobot(borrowId);
        if (map != null) {
            String bankNum = map.get("bankNum").toString();
            String cardNum = map.get("cardNum").toString();
            String birthday = com.jhh.dc.baika.common.util.DateUtil.getDateStringyyyymmdd((Date) map.get("birthday"));
            JSONObject json = new JSONObject();
            json.put("csv_phone_num", map.get("phone"));
            json.put("csv_digit_4", cardNum.substring(cardNum.length() - 4, cardNum.length()));
            json.put("csv_birth_date", birthday);
            //json.put("csv_phone_type", map.get("prodType"));
            if ("ios".equals(map.get("prodType"))) {
                json.put("csv_phone_type", "苹果手机");
            } else {
                json.put("csv_phone_type", "安卓手机");
            }
            if ("1".equals(map.get("proId") + "") || "2".equals(map.get("proId") + "")) {
                json.put("csv_phone_date", "28");
            } else {
                json.put("csv_phone_date", "7");
            }
            //json.put("csv_borrow_amt", ((BigDecimal)map.get("borrAmount")).intValue() + "");
            //借款额度
            json.put("csv_limit", ((BigDecimal) map.get("borrAmount")).intValue() + "");
            json.put("csv_bank_card", bankNum.substring(bankNum.length() - 4, bankNum.length()));
            //json.put("csv_borrow_period", map.get("termValue") + "");
            return json.toString();
        }
        return null;
    }
}
