package com.jhh.dc.baika.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.forkjoin.ForkJoinTask;
import com.jhh.dc.baika.manage.forkjoin.NodeTask;
import com.jhh.dc.baika.manage.service.user.UserService;
import com.jhh.dc.baika.api.constant.StateCode;
import com.jhh.dc.baika.api.loan.CompanyAService;
import com.jhh.dc.baika.common.constant.Constants;
import com.jhh.dc.baika.common.util.CodeReturn;
import com.jhh.dc.baika.common.util.EnumUtils;
import com.jhh.dc.baika.common.util.MD5Util;
import com.jhh.dc.baika.common.util.RedisConst;
import com.jhh.dc.baika.entity.app.Person;
import com.jhh.dc.baika.entity.common.ResponseCode;
import com.jhh.dc.baika.entity.enums.BorrowStatusEnum;
import com.jhh.dc.baika.entity.loan.SystemUser;
import com.jhh.dc.baika.entity.manager.CodeValue;
import com.jhh.dc.baika.entity.manager_vo.CardPicInfoVo;
import com.jhh.dc.baika.entity.manager_vo.PrivateVo;
import com.jhh.dc.baika.manage.entity.Result;
import com.jhh.dc.baika.manage.entity.RiskBlacklistInfoLog;
import com.jhh.dc.baika.manage.mapper.CodeValueMapper;
import com.jhh.dc.baika.manage.mapper.OrderMapper;
import com.jhh.dc.baika.manage.mapper.PersonMapper;
import com.jhh.dc.baika.manage.mapper.SystemUserMapper;
import com.jhh.dc.baika.manage.utils.Detect;
import com.jhh.pay.driver.pojo.BindChannelsRequest;
import com.jhh.pay.driver.pojo.QueryBindChannelResp;
import com.jhh.pay.driver.service.TradeService;
import com.jinhuhang.risk.dto.QueryResultDto;
import com.jinhuhang.risk.dto.blacklist.jsonbean.BlackListDto;
import com.jinhuhang.risk.dto.plan.jsonbean.RiskPerNodeInfoDto;
import com.jinhuhang.risk.service.impl.blacklist.BlacklistAPIClient;
import com.jinhuhang.risk.service.impl.node.UserServiceClient;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.JedisCluster;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

/**
 * 用户相关
 *
 * @author
 */
@Service
@Setter
@Log4j
public class UserServiceImpl implements UserService {

    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SystemUserMapper collectorsMapper;

    @Autowired
    private CompanyAService companyAService;

    @Autowired
    private BlacklistAPIClient riskClient;

    @Autowired
    private TradeService tradeService;

    private static final String RISK_SUCCESS = "1";


    private UserServiceClient nodeClient=new UserServiceClient();

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private CodeValueMapper codeValueMapper;

    @Override
    public Response getUserInfo(Integer perId) {
        Response response = new Response().code(ResponseCode.FIAL);
        Person query = personMapper.selectByPrimaryKey(perId);
        if(query==null){
            return response;
        }
        PrivateVo privateVo=companyAService.queryUserPrivateByPhone(query.getPhone());
        if(null!=privateVo) {
            setBlackList(privateVo);
            try {
                if (privateVo.getImageZ() == null && privateVo.getImageZ() != null) {
                    String path = privateVo.getImageZ();
                    if (path != null) {
                        privateVo.setImageZ("/loan-manage/proxy/image.action?path=" + URLEncoder.encode(path, "UTF-8"));
                    }
                }
                if (privateVo.getImageF() == null && privateVo.getImageF() != null) {
                    String path = privateVo.getImageF();
                    if (path != null) {
                        privateVo.setImageF("/loan-manage/proxy/image.action?path=" + URLEncoder.encode(path, "UTF-8"));
                    }
                }
            }catch (Exception e){
                log.error("获取用户信息异常,{}",e);
            }
            response.data(privateVo).msg("success").code(ResponseCode.SUCCESS);
        }
        return response;
    }

    @Override
    public Response getIdentityCard(Integer perId) {
        Response response = new Response().code(ResponseCode.FIAL);
        CardPicInfoVo cardPicInfoVo = personMapper.getCardPicById(perId);
        if (cardPicInfoVo != null) {
            try {
                if (cardPicInfoVo.getImageZ() == null && cardPicInfoVo.getImageUrlZ() != null) {
                    String path = cardPicInfoVo.getImageUrlZ();
                    if (path != null) {
                        cardPicInfoVo.setImageZ("/loan-manage/proxy/image.action?path=" + URLEncoder.encode(path, "UTF-8"));
                    }
                }
                if (cardPicInfoVo.getImageF() == null && cardPicInfoVo.getImageUrlF() != null) {
                    String path = cardPicInfoVo.getImageUrlF();
                    if (path != null) {
                        cardPicInfoVo.setImageF("/loan-manage/proxy/image.action?path=" + URLEncoder.encode(path, "UTF-8"));
                    }
                }
                response.data(cardPicInfoVo).msg("success").code(ResponseCode.SUCCESS);
            } catch (UnsupportedEncodingException e) {
                response.data(cardPicInfoVo).msg("fail").code(ResponseCode.FIAL);
            }
        }
        return response;
    }

    @Override
    public String getNameByPersonId(Integer personId) {
        if (Detect.isPositive(personId)) {
            Person person = personMapper.selectByPrimaryKey(personId);
            return person != null ? person.getName() : "";
        }
        return "";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = java.lang.Exception.class)
    public Response userBlockWhite(Integer personId, String operatorNum, String operator, String reason, Integer type) throws Exception {
        Response response = new Response().code(ResponseCode.FIAL).msg("操作失败");
        if (!Detect.isPositive(personId)) {
            response.code(ResponseCode.FIAL).msg("拉黑用户不能为空");
            return response;
        }
        if (type < 0) {
            response.code(ResponseCode.FIAL).msg("操作类型为空");
            return response;
        }
        if (reason.length() > 250) {
            response.code(ResponseCode.FIAL).msg("操作理由超过字数限制(不能超过250个字符)");
            return response;
        }

        Person person = personMapper.selectByPrimaryKey(personId);
        if (!Detect.notEmpty(operator)) {
            SystemUser collectors = new SystemUser();
            collectors.setUserSysno(operatorNum);
            collectors = collectorsMapper.selectOne(collectors);
            operator = collectors.getUserName();
        }

        BlackListDto blackListDto = new BlackListDto();
        blackListDto.setCreateTime(Calendar.getInstance().getTime());
        blackListDto.setReason(reason);
        blackListDto.setType(type);
        blackListDto.setHandlerName(operator);
        blackListDto.setHandlerNo(operatorNum);
        blackListDto.setIdcard(person.getCardNum());
        blackListDto.setPhone(person.getPhone());
        blackListDto.setName(person.getName());
        blackListDto.setSys("dc_loan");

        log.info(String.format("------->调用风控拉黑/洗白接口 入参【%s】", JSON.toJSONString(blackListDto)));
        QueryResultDto queryResultDto = riskClient.blacklist(blackListDto);
        log.info(String.format("------->调用风控拉黑/洗白接口 返回【%s】", JSON.toJSONString(queryResultDto)));

        if (queryResultDto == null) {
            return response;
        }

        if (RISK_SUCCESS.equals(queryResultDto.getCode())) {
            response.code(ResponseCode.SUCCESS).msg("操作成功");
        }

        return response;
    }

    @Override
    public Response getBlackList(Integer personId) {

        if (!Detect.isPositive(personId)) {
            return new Response().code(ResponseCode.SUCCESS).data(null);
        }

        Person person = personMapper.selectByPrimaryKey(personId);

        if (ObjectUtils.isEmpty(person)) {
            return new Response().code(ResponseCode.FIAL).msg("用户不存在").data(null);
        }

        // 调用风控黑名单新接口，添加手机号码
        QueryResultDto result = null;
        try {
            result = riskClient.blacklistLogQueryByIdCard(person.getCardNum());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用风控黑名单接口失败,身份证号码 = " + person.getCardNum() + ",手机号码= " + person.getPhone());
        }

        // 调用风控黑名单接口失败|或者返回的结果为空，这里做返回
        if (ObjectUtils.isEmpty(result)) {
            return new Response().code(ResponseCode.SUCCESS).data(null);
        }

        if (!RISK_SUCCESS.equals(result.getCode())) {
            return new Response().code(ResponseCode.SUCCESS).data(null);
        }

        List<RiskBlacklistInfoLog> list = (List) result.getModel();
        return new Response().code(ResponseCode.SUCCESS).data(list);
    }

    @Override
    public Response getBankList(Integer personId) {
        List banks = null;
        if (Detect.isPositive(personId)) {
            banks = personMapper.getBankByPerId(personId);

            List<String> list = new ArrayList<>();
            banks.stream().forEach(t-> list.add(((Map)t).get("bankNum").toString()));
            log.info("开始查询绑卡信息,银行卡号："+list.toString());
            BindChannelsRequest b = new BindChannelsRequest();
            b.setAppId(com.jhh.dc.baika.api.constant.Constants.PayStyleConstants.DC_BIND_CARD_APPID);
            b.setBankCards(list);
            QueryBindChannelResp queryBindChannelResp = tradeService.queryBindChannel(b);
            if(queryBindChannelResp != null && queryBindChannelResp.getCode().equals("200")){
                for (int i = 0; i < banks.size(); i++) {
                    Map<String, Object> bank = (Map<String, Object>) banks.get(i);
                    String[] bindingList = ((JSONObject)queryBindChannelResp.getData()).getJSONArray((String)bank.get("bankNum")).toArray(new String[0]);
                    bank.put("quickBinding", String.join("，", bindingList));
                }
            }
        }

        return new Response().code(200).data(banks);
    }

    @Override
    public Response getValidBankList(Integer personId) {
        List banks = null;
        if (Detect.isPositive(personId)) {
            banks = personMapper.getBankByPerId(personId);
            banks.removeIf(e -> ((Map) e).get("status").equals("无效卡"));
            List<String> list = new ArrayList<>();
            banks.stream().forEach(t-> list.add(((Map)t).get("bankNum").toString()));
            log.info("开始查询绑卡信息,银行卡号："+list.toString());
            BindChannelsRequest b = new BindChannelsRequest();
            b.setAppId(com.jhh.dc.baika.api.constant.Constants.PayStyleConstants.DC_BIND_CARD_APPID);
            b.setBankCards(list);
            QueryBindChannelResp queryBindChannelResp = tradeService.queryBindChannel(b);
            if(queryBindChannelResp != null && queryBindChannelResp.getCode().equals("200")) {
                for (int i = 0; i < banks.size(); i++) {
                    Map<String, Object> bank = (Map<String, Object>) banks.get(i);
                    String[] bindingList = ((JSONObject) queryBindChannelResp.getData()).getJSONArray((String) bank.get("bankNum")).toArray(new String[0]);
                    bank.put("quickBinding", String.join("，", bindingList));
                }
            }
        }

        return new Response().code(200).data(banks);
    }

    @Override
    public Response getOrderList(Integer personId) {
        Map<String, Object> args = new HashMap<String, Object>();
        Integer[] types = new Integer[]{1, 2, 4, 5, 6, 7, 8, 11, 12, 13, 14, 15, 16, 17, 18};
        args.put("perId", personId);
        args.put("type_s", types);
        List orders = orderMapper.getOrdersByArgs(args);
        return new Response().code(200).data(orders);
    }

    @Override
    public Response getNodeList(Integer personId) {
//        List nodeList = null;
//        if (Detect.isPositive(personId)) {
//            nodeList = personMapper.getNodeByPerId(Integer.toString(personId));
//
//        }
//        return new Response().code(200).data(nodeList);
        List<Map<String,Object>> data = Lists.newArrayList();
        if (Detect.isPositive(personId)) {
            Person person = personMapper.selectByPrimaryKey(personId);
            String cardNum = "0";
            if (StringUtils.isNotEmpty(person.getCardNum())){
                cardNum = person.getCardNum();
            }
            List<RiskPerNodeInfoDto> riskPerNodeInfoDtos = nodeClient.selectPerNodeRecords(cardNum, riskNodeProductId());
            if(riskPerNodeInfoDtos!=null&&riskPerNodeInfoDtos.size()>0){
                for(RiskPerNodeInfoDto risk: riskPerNodeInfoDtos) {
                    Map<String,Object> map = Maps.newHashMap();
                    map.put("id",risk.getId());
                    map.put("nodeName", EnumUtils.getBpmNode(risk.getNodeCode()).name);
                    map.put("nodeStatus",EnumUtils.getRiskBpmNodeStatus(risk.getNodeStatus()).status);
                    map.put("nodeStatusName",EnumUtils.getRiskBpmNodeStatus(risk.getNodeStatus()).statusName);
                    map.put("description",risk.getDescription());
                    map.put("nodeDate",risk.getNodeDate());
                    map.put("updateDate",risk.getUpdateDate());
                    data.add(map);
                }
            }
        }
        return new Response().code(200).data(data);
    }

    @Override
    public Response getNodeDetailList(Integer personId) {
        Map map = new HashMap();
        if (Detect.isPositive(personId)) {
            Person person = personMapper.selectByPrimaryKey(personId);
            if (person != null) {
                //未过期的节点详情数据
                map.put("idCard", person.getCardNum());
                map.put("expires", "2");
                List notExpires = personMapper.getNodeDetailByPerId(map);
                //过期的节点详情数据
                map.put("expires", "1");
                List isExpires = personMapper.getNodeDetailByPerId(map);
                map.put("notExpires", notExpires);
                map.put("isExpires", isExpires);
            }

        }
        return new Response().code(200).data(map);
    }

    /**
     * 查询用户列表
     */
    @Override
    public Response getUsers(Map<String, String[]> args) {
        Map param=getargs(args);
        List<Map> list = personMapper.getUsers(param);
        handlerUserNodeList(list);
        return new Response().code(200).data(handlerUserBlackList(list));
    }


    /**
     * 批量查询认证节点列表数据
     */
    private void handlerUserNodeList(List<Map> userList) {
        List<String> idNumbers = Lists.newArrayList();
        //  入参为空，则返回 2018-4-13 风控黑名单新接口
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        // 迭代取出是否认证节点的用户
        for (Map map : userList) {
            String card_num = String.valueOf(map.get("card_num"));
            if (StringUtils.isNotEmpty(card_num)) {
                idNumbers.add(card_num);
            }
            //判断产品类型
            String borrStatus=String.valueOf(map.get("borrow_status"));
            String productType=String.valueOf(map.get("prod_type"));
            map.put("borrowStatusValue",BorrowStatusEnum.getBorrStatusValue(borrStatus,productType));
        }
        // 用NodeTask 批量查询黑名单，每个任务大小为500
        try {
            List<RiskPerNodeInfoDto> riskPerNodeInfoDtos = new ForkJoinPool(2).invoke(new NodeTask(idNumbers,riskNodeProductId()));
            // 迭代遍历
            if(riskPerNodeInfoDtos!=null&&riskPerNodeInfoDtos.size()>0){
                for (RiskPerNodeInfoDto dto : riskPerNodeInfoDtos) {
                    for (Map map : userList) {
                        if (map.containsValue(dto.getIdNumber())) {
                            map.put("card_num", dto.getIdNumber());
                            map.put("description", dto.getDescription());
                            map.put("node_status", EnumUtils.getRiskBpmNodeStatus(dto.getNodeStatus()).status);
                            map.put("node_code", EnumUtils.getBpmNode(dto.getNodeCode()).id);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("查询风控认证节点接口异常",e);
        }

    }

    public static void main(String[] args){
        List list=new ArrayList();
        list.add("342625199310022557");
         UserServiceClient nodeClient=new UserServiceClient();
        List<RiskPerNodeInfoDto> riskPerNodeInfoDtos = nodeClient.selectRecentPerNodeRecord(list, "76");
        System.out.println(riskPerNodeInfoDtos);
        Map map=Maps.newHashMap();
        // 迭代遍历
        for (RiskPerNodeInfoDto dto : riskPerNodeInfoDtos) {
                    map.put("card_num", dto.getIdNumber());
                    map.put("description", dto.getDescription());
                    map.put("node_status", EnumUtils.getRiskBpmNodeStatus(dto.getNodeStatus()).status);
                    map.put("node_code", EnumUtils.getBpmNode(dto.getNodeCode()).id);
            }
        System.out.println(map);
}
    /**
     * 查询渠道用户列表
     */
    @Override
    public Response getChannelUsers(Map<String, String[]> args) {
        List<Map> list = personMapper.getChannelUsers(getChannelArgs(args));
        return new Response().code(200).data(handlerUserBlackList(list));
    }

    @Override
    public List getSource(String code) {
        return personMapper.getRegisterSource(code);
    }

    @Override
    public String riskNodeProductId() {
        String productId=jedisCluster.get(RedisConst.DC_LOAN_RISK_NODE_KEY);
        if(StringUtils.isEmpty(productId)){
            List<CodeValue> list=codeValueMapper.getCodeValueListByCode(RedisConst.DC_LOAN_RISK_NODE_KEY);
            if(list!=null&&list.size()>0){
                productId=list.get(0).getCodeCode();
                jedisCluster.set(RedisConst.DC_LOAN_RISK_NODE_KEY,productId);
            }
        }
        return productId;
    }

    @Override
    public Map getRiskNodeAndStatus() {
        Map map=Maps.newHashMap();
        List riskNode=new ArrayList();
        List riskNodeStatus=new ArrayList();
        for (Constants.RiskBpmNode bpmNode : Constants.RiskBpmNode.values()) {
             Map node=Maps.newHashMap();
             node.put("id",bpmNode.id);
             node.put("name",bpmNode.name);
             riskNode.add(node);
        }
        for (Constants.RiskBpmNodeStatus riskBpmNodeStatus : Constants.RiskBpmNodeStatus.values()) {
            Map status=Maps.newHashMap();
            status.put("status",riskBpmNodeStatus.status);
            status.put("statusName",riskBpmNodeStatus.statusName);
            riskNodeStatus.add(status);
        }
        map.put("riskNode", riskNode);
        map.put("riskNodeStatus",riskNodeStatus);
        return map;
    }

    /**
     * 组装查询条件: 过滤条件 filter，排序条件 sort
     */
    private Map getargs(Map<String, String[]> args) {
        Iterator<String> keys = args.keySet().iterator();
        Map arg = new HashMap();
        while (keys.hasNext()) {
            String key = keys.next();
            if ("filter".equals(key)) {
                String[] filter = args.get(key);
                if (filter.length > 0 && StringUtils.isNotEmpty(filter[0])) {
                    String st = filter[0];
                    JSONArray js = JSON.parseArray(st);
                    for (int i = 0; i < js.size(); i++) {
                        if (!"and".equals(js.get(i).toString())) {
                            if (js.get(i) instanceof JSONArray) {
                                JSONArray jss = JSON.parseArray(js.get(i)
                                        .toString());
                                if (jss.get(0) instanceof JSONArray) {
                                    JSONArray jsdate = jss;
                                    for (int j = 0; j < jsdate.size(); j++) {
                                        setDate(arg, jsdate.get(j));
                                    }
                                } else {
                                    Object o = jss.get(2);
                                    if (o instanceof JSONObject) {
                                        arg.put(jss.get(0),
                                                ((JSONObject) o).get("value"));
                                    } else {
                                        setDate(arg, jss);
                                        arg.put(jss.get(0), jss.get(2));
                                    }
                                }

                            } else {
                                Object o = js.get(2);
                                if (o instanceof JSONObject) {
                                    arg.put(js.get(0),
                                            ((JSONObject) o).get("value"));
                                } else {
                                    arg.put(js.get(0), js.get(2));
                                }
                                break;
                            }

                        }
                    }

                }
            } else if ("sort".equals(key)) {
                String[] sort = args.get(key);
                if (sort.length > 0 && StringUtils.isNotEmpty(sort[0])) {
                    JSONObject jo = JSON.parseArray(sort[0]).getJSONObject(0);
                    arg.put("selector", jo.get("selector"));
                    arg.put("desc", jo.get("desc"));
                }
            }else{
                String[] sort = args.get(key);
                if (sort.length > 0 && StringUtils.isNotEmpty(sort[0])) {
                    arg.put(key,args.get(key)[0]);
                }

            }

        }
        return arg;
    }

    private Map getChannelArgs(Map<String, String[]> args) {
        Iterator<String> keys = args.keySet().iterator();
        Map arg = new HashMap();
        while (keys.hasNext()) {
            String key = keys.next();
            if ("filter".equals(key)) {
                String[] filter = args.get(key);
                if (filter.length > 0 && StringUtils.isNotEmpty(filter[0])) {
                    String st = filter[0];
                    JSONArray js = JSON.parseArray(st);
                    for (int i = 0; i < js.size(); i++) {
                        if (!"and".equals(js.get(i).toString())) {
                            if (js.get(i) instanceof JSONArray) {
                                JSONArray jss = JSON.parseArray(js.get(i)
                                        .toString());
                                if (jss.get(0) instanceof JSONArray) {
                                    JSONArray jsdate = jss;
                                    for (int j = 0; j < jsdate.size(); j++) {
                                        setDate(arg, jsdate.get(j));
                                    }
                                } else {
                                    Object o = jss.get(2);
                                    if (o instanceof JSONObject) {
                                        arg.put(jss.get(0),
                                                ((JSONObject) o).get("value"));
                                    } else {
                                        setDate(arg, jss);
                                        arg.put(jss.get(0), jss.get(2));
                                    }
                                }

                            } else {
                                Object o = js.get(2);
                                if (o instanceof JSONObject) {
                                    arg.put(js.get(0),
                                            ((JSONObject) o).get("value"));
                                } else {
                                    arg.put(js.get(0), js.get(2));
                                }
                                break;
                            }

                        }
                    }

                }
            } else if ("sort".equals(key)) {
                String[] sort = args.get(key);
                if (sort.length > 0 && StringUtils.isNotEmpty(sort[0])) {
                    JSONObject jo = JSON.parseArray(sort[0]).getJSONObject(0);
                    arg.put("selector", jo.get("selector"));
                    arg.put("desc", jo.get("desc"));
                }
            } else if ("source".equals(key)) {
                String sourceArr[] = args.get(key);
                if (sourceArr.length > 0 && StringUtils.isNotEmpty(sourceArr[0])) {
                    /*JSONObject jsonObject = JSON.parseArray(sourceArr[0]).getJSONObject(0);
                    arg.put("source", jsonObject.get("source"));*/
                    arg.put("source", sourceArr[0].toString());
                }
            }

        }
        return arg;
    }

    private void setDate(Map<String, Object> arg, Object js) {

        if (js instanceof JSONArray) {
            JSONArray jss = (JSONArray) js;
            if (jss.get(1).toString().indexOf(">") > -1) {
                arg.put(jss.getString(0) + "_start", jss.getString(2));
            } else if (jss.get(1).toString().indexOf("<") > -1) {
                arg.put(jss.getString(0) + "_end", jss.getString(2));
            }
        } else {

        }

    }

    /**
     * 根据身份证号码查询该用户是否在黑名单
     *
     * @param privateVo
     */
    private void setBlackList(PrivateVo privateVo) {
        if (StringUtils.isEmpty(privateVo.getCardNum())) {
            return;
        }

        if (ObjectUtils.isEmpty(privateVo)) {
            return;
        }
        try {

            // 调用风控黑名单新接口，添加手机号码
            QueryResultDto result = riskClient.blacklistSingleQuery(privateVo.getCardNum(), privateVo.getPhone());
            if (ObjectUtils.isEmpty(result)) {
                return;
            }

            if ("0".equals(result.getCode())) {
                privateVo.setBlacklist("Y");
            } else {
                privateVo.setBlacklist("N");
            }
        } catch (Exception e) {
            log.error("======调用风控查询黑名单失败===========");
            log.error(e);
        }

    }

    /**
     * 批量查询黑名单列表数据
     *
     * @param list
     */
    private List handlerUserBlackList(List<Map> list) {
        //  入参为空，则返回 2018-4-13 风控黑名单新接口
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }

        List<BlackListDto> blackListDtos = Lists.newArrayList();

        list.forEach(map -> {
            BlackListDto dto = new BlackListDto();
            // 此处不能合并，防止ClassCastException
            if (!ObjectUtils.isEmpty(map.get("card_num"))) {
                dto.setIdcard(map.get("card_num").toString());
            }

            if (!ObjectUtils.isEmpty(map.get("phone"))) {
                dto.setPhone(map.get("phone").toString());
            }
            if (StringUtils.isNotBlank(dto.getPhone()) || StringUtils.isNotBlank(dto.getIdcard())) {
                blackListDtos.add(dto);
            }
        });

        // 风控黑名单手机号或者身份证号为空，则返回
        if (CollectionUtils.isEmpty(blackListDtos)) {
            return list;
        }

        // 用ForkJoinTask 批量查询黑名单，每个任务大小为20000
        List blackList = new ForkJoinPool(2).invoke(new ForkJoinTask(blackListDtos));

        return getList(list, blackList);
    }

    private static List<Map> getList(List<Map> list, List blackList) {
        list.forEach(map -> {
            if(blackList.contains(map.get("phone")+"fail")){
                map.put("blacklist", "P");
            } else if (blackList.contains(map.get("card_num")) || blackList.contains(map.get("phone"))) {
                map.put("blacklist", "Y");
            } else {
                map.put("blacklist", "N");
            }
        });
        return list;
    }

    @Override
    public Result addOfflineUser(String phone) {
        Result result = new Result(CodeReturn.success, StateCode.SUCCESS_MSG);
        Person p = personMapper.getPersonByPhone(phone);
        if (!ObjectUtils.isEmpty(p)) {
            result.setCode(Result.FAIL);
            result.setMessage("该手机用户已注册");
            return result;
        } else {
            String password = MD5Util.encodeToMd5("123456");
            Person user = new Person();
            user.setPhone(phone);
            user.setPayPassword(password);
            user.setIsLogin(1);
            user.setSource("3");
            user.setCreateDate(new Date());
            //注册方法
            int successInt = personMapper.insertSelective(user);
            if (successInt < 1) {
                return new Result(CodeReturn.fail, "注册失败，系统错误!");
            }
        }
        return result;
    }
}
