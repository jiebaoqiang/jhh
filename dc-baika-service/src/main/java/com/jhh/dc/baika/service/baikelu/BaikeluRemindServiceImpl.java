package com.jhh.dc.baika.service.baikelu;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.baikelu.BaikeluRemindService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.common.util.*;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemind;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemindExcelVo;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemindVo;
import com.jhh.dc.baika.mapper.baikelu.BaikeluRemindMapper;
import com.jhh.dc.baika.common.enums.BaikeluRemindEnum;
import com.jhh.dc.baika.common.util.http.AOSHttpClient;
import com.jhh.dc.baika.common.util.http.AOSJson;
import com.jhh.dc.baika.common.util.http.HttpRequestVO;
import com.jhh.dc.baika.common.util.http.HttpResponseVO;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;
import tk.mybatis.mapper.entity.Example;
import java.io.File;
import java.util.*;

/**
 * 百可录提醒电话服务实现类
 */
@Service
public class BaikeluRemindServiceImpl implements BaikeluRemindService {
    private static final Logger logger = LoggerFactory.getLogger(BaikeluRemindServiceImpl.class);

    @Value("${baikelu_ClientName}")
    private String baikelu_ClientName;

    @Value("${baikelu_AccessToken}")
    private String baikelu_AccessToken;

    @Value("${baikelu_JobCode}")
    private String baikelu_JobCode;

    @Value("${baikelu_ApiWays_RemindUrl}")
    private String baikelu_ApiWays_RemindUrl;

    @Value("${baikelu_ExcelWays_RemindUrl}")
    private String baikelu_ExcelWays_RemindUrl;

    //本地excel文件生成路径
    @Value("${baikelu_ExcelWays_promptPath}")
    private String baikelu_ExcelWays_promptPath;

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private BaikeluRemindMapper baikeluRemindMapper;


    /**
     * 电话提醒API形式
     * @param remindVoList 接收对象
     * @param type    内容类型
     * @return
     */
    @Override
    public ResponseDo sendBaikeluRemindApi(List<BaikeluRemindVo> remindVoList, String type) {
        ResponseDo responseDo = ResponseDo.newSuccessDo();
        String baikeluIsSend=jedisCluster.get(RedisConst.BAIKELU_ALL_IS_OPEN_KEY);//redis开关
        //1、数据效验
        if(StringUtils.isEmpty(baikeluIsSend)||!BaikeluRemindEnum.IS_OPEN_BAIKELU.getCode().equals(baikeluIsSend)){
            logger.info("【sendBaikeluRemindExcel】请求接口数据====>百可录接口未打开，查无【dc_baikelu_all_onOff】redis数据");
            responseDo=ResponseDo.newFailedDo("百可录提醒电话redis开关>>>关闭状态");
            return responseDo;
        }
        for(int i=0;i<remindVoList.size();i++){
            BaikeluRemindVo baikeluRemindVo=remindVoList.get(i);
            String workId=createRequestOrderNo();
            Map map=new HashMap();
            Map req=new HashMap();
            Date requestTime=null;
            Date responseTime=null;
            String responseData=null;
            String requestData=null;
            map.put("work_id", workId);
            map.put("order_id", createRequestOrderNo());
            map.put("corp_code", baikelu_ClientName);
            map.put("job_code", baikelu_JobCode);
            map.put("access_token", baikelu_AccessToken);
            requestTime=new Date();
            try {
                requestData=JSONObject.toJSONString(baikeluRemindVo);
                Map dtt=JSONObject.parseObject(requestData, HashMap.class);
                map.put("work_data",dtt);
                req.put("mdata",JSONObject.toJSONString(map));
                HttpRequestVO httpRequestVO = new HttpRequestVO(baikelu_ApiWays_RemindUrl, req);
                HttpResponseVO httpResponseVO = AOSHttpClient.upload(httpRequestVO);
                httpResponseVO.setSendParam(JSONObject.toJSON(req).toString());
                responseData=httpResponseVO.getOut();
            } catch (Exception e) {
                logger.error("【sendBaikeluRemindExcel】百可录异常====>workId:【"+workId+"】",e);
               // e.printStackTrace();
                responseData=e.getMessage();
            }
            responseTime=new Date();
            saveBaikeluRemindToDbObject(workId,baikeluRemindVo.getCsv_phone_num(),type,requestData,responseData,requestTime,responseTime);
        }
         return responseDo;
    }
    /**
     * 电话提醒excel形式
     * @param remindVoList 接收对象
     * @param type    内容类型
     * @return
     */
    @Override
    public ResponseDo sendBaikeluRemindExcel(List<BaikeluRemindExcelVo> remindVoList, String type) {
        ResponseDo responseDo = ResponseDo.newSuccessDo();
        List<BaikeluRemindExcelVo> list=new ArrayList<BaikeluRemindExcelVo>();
        list.addAll(remindVoList);
        //1、参数准备
        Map map = new HashMap();
        Date requestTime;                                                               //a.请求时间
        Date responseTime;                                                              //b.响应时间
        String requestUUID="";                                                          //c.订单号
        String requestData=JSONObject.toJSONString(remindVoList);                       //d.请求数据
        String responseData="";                                                        //e.响应数据
        String baikeluIsSend=jedisCluster.get(RedisConst.BAIKELU_ALL_IS_OPEN_KEY);//f.redis开关
        //2、数据效验
        if(StringUtils.isEmpty(baikeluIsSend)||!BaikeluRemindEnum.IS_OPEN_BAIKELU.getCode().equals(baikeluIsSend)){
            logger.info("【sendBaikeluRemindExcel】请求接口数据====>百可录接口未打开，查无【dc_baikelu_all_onOff】redis数据");
            responseDo=ResponseDo.newFailedDo("百可录提醒电话redis开关>>>关闭状态");
            return responseDo;
        }
        if(remindVoList==null||remindVoList.size()==0){
            responseDo=ResponseDo.newFailedDo("百可录提醒电话list数据为空");
            return responseDo;
        }
        /*======订单号赋值=========*/
        requestUUID=remindVoList.get(0).getJob_ref();
        logger.info("【sendBaikeluRemindExcel】请求接口数据====>requestUUID:【"+requestUUID+"】,remindVoList:【"+JSONObject.toJSONString(remindVoList)+"】,type【"+type+"】");

        //3、封装百可录请求参数
        requestTime=new Date();
        try {
            map.put("client_name", baikelu_ClientName);
            map.put("access_token", baikelu_AccessToken);
            map.put("file","file");
            String fileName = ExcelUtils.createExcel(remindVoList, baikelu_ExcelWays_promptPath, baikelu_JobCode+"-" + DateUtil.getDateString(new Date()),"DataSet");
            File file = new File(baikelu_ExcelWays_promptPath + fileName);
            responseData= HttpUtils.postToFile(baikelu_ExcelWays_RemindUrl,  map, file);
            logger.info("【sendBaikeluRemindExcel】百可录响应数据====>requestUUID:【"+requestUUID+"】,result:【"+responseData+"】");
            if(Detect.notEmpty(responseData)){
                if(responseData.contains(fileName+"上传成功！")){
                    responseDo = ResponseDo.newSuccessDo(fileName+"上传成功！");
                } else{
                    responseDo=ResponseDo.newFailedDo("百可录请求失败");
                    responseDo.setData(responseData);
                }
            }else{
                responseDo=ResponseDo.newFailedDo("百可录请求失败");
                responseDo.setData("百可录响应数据为空");
            }
        } catch (Exception e) {
            logger.error("【sendBaikeluRemindExcel】百可录异常====>requestUUID:【"+requestUUID+"】",e);
            responseData=e.fillInStackTrace().toString();
            responseDo=ResponseDo.newFailedDo("百可录请求失败");
            responseDo.setData(e);

        }
        responseTime=new Date();
        saveBaikeluRemindToDb(list,type,requestData,responseData,requestTime,responseTime);
        logger.info("【sendBaikeluRemindExcel】接口响应数据====>requestUUID:【"+requestUUID+"】,responseDo:【"+JSONObject.toJSONString(responseDo)+"】");
        return responseDo;
    }

    //保存百可录请求流水
    private void saveBaikeluRemindToDb(List<BaikeluRemindExcelVo> list,String remindType,String requestData,String responseData,Date requestTime,Date responseTime){
        try {
            List<BaikeluRemind> insertList=new ArrayList<>();
            for(int i=0;i<list.size();i++){
                BaikeluRemindExcelVo baikeluRemindExcelVo=list.get(i);
                BaikeluRemind baikeluRemind = new BaikeluRemind();
                baikeluRemind.setPhone(baikeluRemindExcelVo.getPhone_num());
                baikeluRemind.setJobRef(baikeluRemindExcelVo.getJob_ref());
                baikeluRemind.setCreateTime(new Date());
                baikeluRemind.setRemindType(remindType);
                baikeluRemind.setRemindDesc(BaikeluRemindEnum.getDescByCode(remindType));
                baikeluRemind.setRequestData(requestData);
                baikeluRemind.setResponseData(responseData);
                baikeluRemind.setRequestTime(requestTime);
                baikeluRemind.setResponseTime(responseTime);
                insertList.add(baikeluRemind);
            }
            baikeluRemindMapper.insertBaikeluRemindList(insertList);
        }catch (Exception  e){
            logger.error("保存百可录请求流水【saveBaikeluRemindToDb】异常:",e);
        }
    }

    //保存百可录请求流水
    private void saveBaikeluRemindToDbObject(String jobRef,String phone,String remindType,String requestData,String responseData,Date requestTime,Date responseTime){
        try {
                BaikeluRemind baikeluRemind = new BaikeluRemind();
                baikeluRemind.setPhone(phone);
                baikeluRemind.setJobRef(jobRef);
                baikeluRemind.setCreateTime(new Date());
                baikeluRemind.setRemindType(remindType);
                baikeluRemind.setRemindDesc(BaikeluRemindEnum.getDescByCode(remindType));
                baikeluRemind.setRequestData(requestData);
                baikeluRemind.setResponseData(responseData);
                baikeluRemind.setRequestTime(requestTime);
                baikeluRemind.setResponseTime(responseTime);
                baikeluRemindMapper.insert(baikeluRemind);
        }catch (Exception  e){
            logger.error("保存百可录请求流水【saveBaikeluRemindToDbObject】异常:",e);
        }
    }

    /**
     * 百可录开关控制
     * @param redisKey
     * @param flag true开  false
     * @return
     */
    @Override
    public ResponseDo baikeluOnOffControl(String redisKey, String flag) {
        ResponseDo responseDo=ResponseDo.newSuccessDo(redisKey+"设置成功");
        String status=jedisCluster.set(redisKey,flag);
        if("OK".equals(status)){
            responseDo=ResponseDo.newSuccessDo(redisKey+"设置成功,设置值为:"+jedisCluster.get(redisKey));
            return responseDo;
        }
        return ResponseDo.newFailedDo(redisKey+"设置失败");
    }

    /**
     * 百可录回调函数
     * @param
     * @return
     */
    @Override
    public ResponseDo baikeluCallbackRemind(String jobData) {
        ResponseDo responseDo=ResponseDo.newFailedDo("回调失败");
        logger.info("百可录回调函数【baikeluCallbackRemind】请求数据:jobData:"+jobData+"");
        if(StringUtils.isEmpty(jobData)){
               logger.info("百可录回调函数【baikeluCallbackRemind】jobData数据为空");
               responseDo=ResponseDo.newFailedDo("回调失败,jobData数据为空");
               return responseDo;
        }
        JSONObject result=JSONObject.parseObject(jobData);
        if(StringUtils.isEmpty(result.getString("biocloo_response"))){
            logger.info("百可录回调函数【baikeluCallbackRemind】biocloo_response数据为空");
            responseDo=ResponseDo.newFailedDo("回调失败,biocloo_response数据为空");
            return responseDo;
        }

        String jsonData=result.getString("biocloo_response");
        JSONArray jsonArray=new JSONArray();
        if(jsonData.substring(0,1).equals("{")){
            JSONObject jsonObject=JSONObject.parseObject(jsonData);
            jsonArray.add(jsonObject);
        }else {
            jsonArray = JSONArray.parseArray(result.getString("biocloo_response"));
        }
        if(jsonArray==null||jsonArray.size()==0){
            logger.info("百可录回调函数【baikeluCallbackRemind】biocloo_response数据为空");
            responseDo=ResponseDo.newFailedDo("回调失败,biocloo_response数据为空");
            return responseDo;
        }
        List<BaikeluRemind> updateList=new ArrayList<>();
        Date callbackTime=new Date();
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
            String jobRef=jsonObject.getString("job_ref");
            Example example=new Example(BaikeluRemind.class);
            example.createCriteria().andEqualTo("jobRef",jobRef);
            List<BaikeluRemind> list=baikeluRemindMapper.selectByExample(example);
            if(null==list||list.size()==0){
                continue;
            }
            BaikeluRemind baikeluRemind=list.get(0);
            baikeluRemind.setCallbackData(jsonObject.toJSONString());
            baikeluRemind.setCallbackTime(callbackTime);
            updateList.add(baikeluRemind);
        }
        baikeluRemindMapper.updateBaikeluRemindList(updateList);
        responseDo=ResponseDo.newSuccessDo("回调成功");
        return responseDo;
    }

    @Override
    public String baikeluCallbackRemindApi(String jobRef,String workResult) {
        logger.info("百可录回调函数【baikeluCallbackRemind】请求数据:jobRef:"+jobRef+",workResult:"+workResult+"");

        if(StringUtils.isEmpty(jobRef)){
            logger.info("百可录回调函数【baikeluCallbackRemind】jobRef数据为空");
            return this.baikeluCallBackFailApi(BaikeluRemindEnum.FAIL_ONE.getCode(),"jobRef数据为空");
        }
        if(StringUtils.isEmpty(workResult)){
            logger.info("百可录回调函数【baikeluCallbackRemind】workResult数据为空");
            return this.baikeluCallBackFailApi(BaikeluRemindEnum.FAIL_ONE.getCode(),"workResult数据为空");
        }
        Date callbackTime=new Date();
        Example example=new Example(BaikeluRemind.class);
        example.createCriteria().andEqualTo("jobRef",jobRef);
        List<BaikeluRemind> list=baikeluRemindMapper.selectByExample(example);
        if(null!=list&&list.size()>0){
            BaikeluRemind baikeluRemind=list.get(0);
            baikeluRemind.setCallbackData(workResult);
            baikeluRemind.setCallbackTime(callbackTime);
            baikeluRemindMapper.updateByPrimaryKeySelective(baikeluRemind);
        }
        return this.baikeluCallBackSuccApi();
    }
    /**
     * 百可录异常报文返回Api形式
     */
    @Override
    public String baikeluCallBackFailApi(String code,String result) {
        Map child=new HashMap();
        child.put("error_code",code);
        child.put("error_msg",result);
        String requestData=JSONObject.toJSONString(child);
        Map dtt=JSONObject.parseObject(requestData, HashMap.class);
        Map respones=new HashMap();
        respones.put("failure",dtt);
        return JSONObject.toJSONString(respones);
    }
    /**
     * 百可录正常报文返回Api形式
     */
    @Override
    public String baikeluCallBackSuccApi() {
        Map child=new HashMap();
        child.put("received_time",DateUtil.getDateTime_String(new Date()));
        String requestData=JSONObject.toJSONString(child);

        Map dtt=JSONObject.parseObject(requestData, HashMap.class);
        Map respones=new HashMap();
        respones.put("success",dtt);
        return JSONObject.toJSONString(respones);
    }

    //创建请求批次订单号
    private String createRequestOrderNo(){
        String uuId=UUID.randomUUID().toString();
        uuId=uuId.replace("-", "");
        return uuId;
    }
    public static void main(String[] args) {
        JSONObject param=new JSONObject();
        JSONObject result=new JSONObject();
        param.put("received_time",DateUtil.getDateTime_String(new Date()));
        result.put("success",param);
        System.out.println(result);
        String json="{\"biocloo_response\":{\"job_result\":\"部分接听\",\"phone_number\":\"17717374735\",\"job_result_code\":\"partial_passed\",\"calling_list\":[{\"calling_start_time\":\"2018-06-05 12:34:54\",\"calling_status\":\"部分接听\",\"calling_status_code\":\"partial_passed\",\"calling_duration\":\"6.82\"}],\"job_ref\":\"f67eb04a-2f28-4334-8ac2-0e5db23e8442\"}}";
         System.out.println(json);
      /* List<Integer> ss=null;
       //System.out.println(JSONObject.toJSONString(ss));
        String json="{\"biocloo_response\":{\"job_result\":\"部分接听\",\"phone_number\":\"17717374735\",\"job_result_code\":\"partial_passed\",\"calling_list\":[{\"calling_start_time\":\"2018-06-05 12:34:54\",\"calling_status\":\"部分接听\",\"calling_status_code\":\"partial_passed\",\"calling_duration\":\"6.82\"}],\"job_ref\":\"f67eb04a-2f28-4334-8ac2-0e5db23e8442\"}}";
        JSONObject result=JSONObject.parseObject(json);
        String jsonData=result.getString("biocloo_response");
        JSONArray jsonArray=new JSONArray();
        if(jsonData.substring(0,1).equals("{")){
            JSONObject jsonObject1=JSONObject.parseObject(jsonData);
            jsonArray.add(jsonObject1);
        }else {
            jsonArray = JSONArray.parseArray(result.getString("biocloo_response"));
        }
      System.out.println(jsonArray.toJSONString());*/
//        Map map=new HashMap();
//        Map child=new HashMap();
//        child.put("received_time",DateUtil.getDateTime_String(new Date()));
//        String response=JSONObject.toJSONString(child);
//        Map dtt=JSONObject.parseObject(response, HashMap.class);
//        map.put("success",dtt);
//      System.out.println(JSONObject.toJSONString(map));



    }
}
