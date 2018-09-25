package com.jhh.dc.baika.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.jhh.dc.baika.manage.entity.PolyXinLi;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.mapper.CollectorsRemarkMapper;
import com.jhh.dc.baika.manage.mapper.OrderMapper;
import com.jhh.dc.baika.manage.utils.Assertion;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.api.channel.AgentChannelService;
import com.jhh.dc.baika.api.constant.Constants;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.common.ResponseCode;
import com.jhh.dc.baika.entity.loan.ExportWorkReport;
import com.jhh.dc.baika.entity.manager.Order;
import com.jhh.dc.baika.manage.mapper.CollectorsListMapper;
import com.jhh.dc.baika.manage.mapper.PersonMapper;
import com.jhh.dc.baika.manage.service.loan.LoanService;
import com.jhh.dc.baika.common.util.Detect;
import com.jhh.dc.baika.common.util.HttpUtils;
import com.jhh.dc.baika.common.util.PropertiesReaderUtil;
import com.jhh.dc.baika.common.util.RedisConst;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.net.URLDecoder;
import java.util.*;

@Service("loanService")
public class LoanServiceImpl implements LoanService {

    private Logger logger = Logger.getLogger(LoanServiceImpl.class);

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    CollectorsRemarkMapper collectorsRemarkMapper;

    @Autowired
    CollectorsListMapper collectorsListMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    AgentChannelService agentChannelService;

    @Autowired
    private JedisCluster jedisCluster;

    private static final String POLY_XINLI_CREDIT_URL = PropertiesReaderUtil.read("system", "RC_CREDIT_URL");


    @Override
    public List getPolyXinliCredit(Integer perId, int offset, int size) {
        Person person = personMapper.selectByPrimaryKey(perId);
        if (person != null) {
            logger.info("getPolyXinliCredit:-----------idCard:" + person.getCardNum() + "-----name:" + person.getName());
            String result = "";
            try {
                StringBuffer params = new StringBuffer();
                params.append("phone=").append(person.getPhone());
                params.append("&idcard=").append(person.getCardNum());
                result = HttpUtils.sendGet(POLY_XINLI_CREDIT_URL, params.toString());
                result = URLDecoder.decode(result, "UTF-8");
            } catch (Exception e) {
                logger.error("无法获取通话记录", e);
            }

            if (Detect.notEmpty(result)) {
                try {
                    JSONArray jsonArray = new JSONArray();

                    List<PolyXinLi> resultArray = JSONObject.parseArray(((JSONObject) JSON.parse(result)).getString("model"), PolyXinLi.class);
                    if (resultArray != null && resultArray.size() > 0) {
                        //排序
                        Collections.sort(resultArray);

                        Map<String, String> contactJsons = getContact(person.getContactUrl());

                        for (int i = offset; i < offset + size && i < resultArray.size(); i++) {
                            PolyXinLi item = resultArray.get(i);
                            item.setYMName(contactJsons.get(item.getPhone_num()));
                            jsonArray.add(item);
                        }

                        return getPageObject(jsonArray, offset, size, resultArray.size());
                    }
                } catch (Exception e) {
                    logger.error("无法解析通话记录", e);
                }
            }
        }
        return null;
    }

    @Override
    public List getContact(Integer perId, int offset, int size) {
        logger.info("getContact:-----------perId:" + perId);

        if (Detect.isPositive(perId)) {
            Person person = personMapper.selectByPrimaryKey(perId);
            if (person != null && person.getContactUrl() != null) {
                Map<String, String> resultArray = getContact(person.getContactUrl());
                if (resultArray != null) {
                    JSONArray jsonArray = new JSONArray();
                    String[] keys = resultArray.keySet().toArray(new String[0]);
                    for (int i = offset; i < offset + size && i < keys.length; i++) {
                        String phone = keys[i];
                        String name = resultArray.get(phone);
                        JSONObject item = new JSONObject();
                        item.put("phone", phone);
                        item.put("name", name);
                        jsonArray.add(item);
                    }
                    return getPageObject(jsonArray, offset, size, resultArray.size());
                }
            }
        }
        return null;
    }

    public Map<String, String> getContact(String contractUrl) {
        String result = HttpUtils.sendGet(contractUrl, null);
        Map<String, String> contractMap = new LinkedHashMap<>();
        if (Detect.notEmpty(result)) {
            JSONArray array = JSONObject.parseArray(result);
            Iterator<Object> iter = array.iterator();
            while (iter.hasNext()) {
                JSONObject object = (JSONObject) iter.next();
                contractMap.put(object.getString("phone"), object.getString("name"));
            }
        }
        return contractMap;
    }

    @Override
    public List<ExportWorkReport> workReport(Map<String, Object> map) {
        return collectorsListMapper.getWorkReport(map);
    }

    @Override
    public List getOrderList(Map<String, String[]> param, Integer borrId) {
        Map<String, Object> paramMap = QueryParamUtils.getargs(param);
        String typeWithChannel = (String) paramMap.get("typeWithChannel");
        if (Detect.notEmpty(typeWithChannel)) {
            String[] splits = typeWithChannel.split("/", -1);
            paramMap.put("type", splits[0]);
            if (splits.length > 1 && splits[1] != null) {
                paramMap.put("channel", splits[1].trim());
            } else if (Arrays.asList("15", "16", "17", "18").contains(splits[0])) {
                paramMap.put("filterNull", true);
            }
        }
        paramMap.put("contractId", borrId);
        Integer[] types = new Integer[]{2, 4, 5, 6, 7, 8, 12, 13, 14, 16, 17, 18};
        paramMap.put("type_s", types);
        List orders = orderMapper.getOrdersByArgs(paramMap);
        return orders;
    }

    @Override
    public Response getOrderState(String serialNo) throws Exception {
        Assertion.notEmpty(serialNo, "订单号不能为空");
        Response response = new Response().code(ResponseCode.FIAL).msg("系统异常，请稍后在试");

        Order order = orderMapper.selectBySerial(serialNo);

        // 以流水单号为维度设置每2min只能查询一次
        // 判断是否有时间限制key
        String queryTime = jedisCluster.get(RedisConst.ORDER_HISTORY_QUERY_TIME);
        if (StringUtils.isEmpty(queryTime)) {
            // 默认设置2min
            queryTime = "120";
            jedisCluster.set(RedisConst.ORDER_HISTORY_QUERY_TIME, queryTime);
        }

        long querySeconds = Long.parseLong(queryTime);
        long queryMinutes = querySeconds / 60;

        // 判断当前时间是否超过2min
        long currentTime = System.currentTimeMillis();
        long afterTime = (currentTime - order.getCreationDate().getTime()) / 1000 / 60;
        if (afterTime < queryMinutes) {
            response.msg("请" + (queryMinutes - afterTime) + "分钟后查询!");
            return response;
        }

        String serialKey = new StringBuilder(RedisConst.ORDER_HISTORY_KEY).append(":").append(serialNo).toString();
        if (jedisCluster.exists(serialKey)) {
            response.msg(queryMinutes + "分钟内只能查询一次，请稍后再试");
            return response;
        } else {
            jedisCluster.set(serialKey, serialNo, "NX", "EX", querySeconds);
        }
        ResponseDo responseDo = agentChannelService.state(serialNo);
        if (responseDo.getCode() == Constants.DeductQueryResponseConstants.SUCCESS_CODE) {
            response.code(ResponseCode.SUCCESS).msg("第三方交易成功");
        } else if (responseDo.getCode() == Constants.DeductQueryResponseConstants.PROGRESSING) {
            response.code(ResponseCode.SUCCESS).msg(responseDo.getInfo());
        } else if (responseDo.getCode() == Constants.DeductQueryResponseConstants.ORDER_FAIL_SETTLE_FAIL ||
                responseDo.getCode() == Constants.DeductQueryResponseConstants.ORDER_FAIL_SETTLE_SUCCESS ||
                responseDo.getCode() == Constants.DeductQueryResponseConstants.SUCCESS_ORDER_SETTLE_FAIL) {
            response.code(ResponseCode.FIAL).msg(responseDo.getInfo());
        } else {
            response.code(responseDo.getCode());
            response.setMsg(responseDo.getInfo());
            response.setData(responseDo.getData());
        }
        return response;
    }

    @Override
    public List getMemos(Map<String, String[]> param, Integer borrId) {
        Map<String, Object> paramMap = QueryParamUtils.getargs(param);
        paramMap.put("contractSysno", borrId);

        return collectorsRemarkMapper.selectRemarkInfo(paramMap);
    }

    public Page getPageObject(List list, int offset, int size, int totalSize) {
        Page page = new Page();
        page.add(list);
        page.setCount(true);
        page.setPageNum(offset / size + 1);
        page.setPageSize(size);
        page.setStartRow(offset);
        page.setEndRow(offset + size);
        page.setTotal(totalSize);
        page.setPages(totalSize / size);
        page.setReasonable(false);
        page.setPageSizeZero(false);
        return page;
    }
}
