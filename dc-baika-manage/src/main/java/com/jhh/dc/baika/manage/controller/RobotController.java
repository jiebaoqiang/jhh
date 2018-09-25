package com.jhh.dc.baika.manage.controller;


import com.alibaba.fastjson.JSONObject;
import com.jhh.dc.baika.api.baikelu.BaikeluRemindService;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemind;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemindExcelVo;
import com.jhh.dc.baika.entity.baikelu.BaikeluRemindVo;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.service.robot.RobotService;
import com.jhh.dc.baika.common.enums.BaikeluRemindEnum;
import com.jhh.dc.baika.common.util.HttpUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 机器人入口
 * carl.wan
 * 2017年9月12日 09:54:52
 */
@Controller
public class RobotController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(RobotController.class);
	@Autowired
	private RobotService robotService;

	@Autowired
	private BaikeluRemindService baikeluRemindService;


	/**
	 * 给百可录发送首单审核数据
	 * @param borrId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/robot/rcOrder" ,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Response sendOrder(Integer borrId) throws Exception {
		logger.info("/robot/rcOrder begin" + borrId);
		Response response = robotService.sendRcOrder(borrId);
		return  response;
	}

	/**
	 * 百可录回调请求
	 * @param request
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/robot/callBack/rcOrder" ,method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public String callBackRcOrder(HttpServletRequest request) throws Exception {
		logger.info("callBackRcOrder start");
		Response response = robotService.callBackRc(request);
		if(response != null && response.getData() != null){
			logger.info("callBackRcOrder end" + JSONObject.toJSONString(response));
            return response.getData().toString();
        }
        logger.info("callBackRcOrder end" + JSONObject.toJSONString(response));
        return JSONObject.toJSONString(response);
	}

	/**
	 * 本地查询百可录打电话信息
	 * @param request
	 * @param borrNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/robotOrder/{borrNum}" ,method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Response queryRobotOrder(HttpServletRequest request, @PathVariable String borrNum) throws Exception {

		return robotService.robotOrderByBorrNum(borrNum, request);
	}

	/**
	 * 百可录提醒电话开关设置
	 * @param1.总开关redisKey：dc_baikelu_all_onOff  2.flag:true打开，false关闭
	 * @throws Exception
	 */
	@RequestMapping(value = "/baikelu/setBaikeluOnOff/{redisKey}/{flag}")
	@ResponseBody
	public String queryRobotOrder(HttpServletRequest request, @PathVariable String redisKey, @PathVariable String flag) throws Exception {
		String result=JSONObject.toJSONString(baikeluRemindService.baikeluOnOffControl(redisKey,flag));
		return result;
	}

	/**
	 * 百可录提醒电话接口调试Excel形式
	 */
	@RequestMapping(value = "/baikelu/sendBaikeluRemindExcel")
	@ResponseBody
	public String sendBaikeluRemindExcel(HttpServletRequest request) throws Exception {
		List<BaikeluRemindExcelVo> remindVoList=new ArrayList<BaikeluRemindExcelVo>();
		BaikeluRemindExcelVo baikeluRemindExcelVo=new BaikeluRemindExcelVo();
		baikeluRemindExcelVo.setJob_ref(UUID.randomUUID().toString());
		baikeluRemindExcelVo.setPhone_num("17717374735");
		baikeluRemindExcelVo.setRemind_type(BaikeluRemindEnum.NO_DOWNAPP_REMIND.getStatus());
		BaikeluRemindExcelVo baikeluRemindExcelVo1=new BaikeluRemindExcelVo();
		baikeluRemindExcelVo1.setJob_ref(UUID.randomUUID().toString());
		baikeluRemindExcelVo1.setPhone_num("13122578489");
		baikeluRemindExcelVo1.setRemind_type(BaikeluRemindEnum.NO_LOGIN_REMIND.getStatus());
		remindVoList.add(baikeluRemindExcelVo);
		String result=JSONObject.toJSONString(baikeluRemindService.sendBaikeluRemindExcel(remindVoList, BaikeluRemindEnum.NO_DOWNAPP_REMIND.getCode()));
		return result;
	}
	/**
	 * 百可录提醒电话接口调试api形式
	 */
	@RequestMapping(value = "/baikelu/sendBaikeluRemindApi")
	@ResponseBody
	public String sendBaikeluRemindApi(HttpServletRequest request) throws Exception {
		List<BaikeluRemindVo> remindVoList=new ArrayList<BaikeluRemindVo>();
		BaikeluRemindVo baikeluRemind=new BaikeluRemindVo();
		baikeluRemind.setCsv_arn(UUID.randomUUID().toString());
		baikeluRemind.setCsv_phone_num("17717374735");
		baikeluRemind.setCsv_tag(BaikeluRemindEnum.NO_LOGIN_REMIND.getStatus());
		remindVoList.add(baikeluRemind);
		String result=JSONObject.toJSONString(baikeluRemindService.sendBaikeluRemindApi(remindVoList, BaikeluRemindEnum.NO_LOGIN_REMIND.getCode()));
		return result;
	}

	/**
	 * 百可录通知类电话回调请求Excel形式
	 * @param request
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/baikelu/callBack/remind" ,method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public String callBackRemind(HttpServletRequest request) throws Exception {
		logger.info("callBackRemind start");
		String jobData="";
		ResponseDo responseDo=ResponseDo.newFailedDo("回调异常");
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> fileItems = upload.parseRequest(request);
			for (FileItem fi : fileItems) {
				if (fi.isFormField() && fi.getFieldName().equals("job_data")) {
					jobData = fi.getString("UTF-8");
					break;
				}
			}
		}catch (Exception  e){
			e.printStackTrace();
			logger.info("callBackRemind接口异常:",e);
			return JSONObject.toJSONString(responseDo);
		}
		String response= JSONObject.toJSONString(baikeluRemindService.baikeluCallbackRemind(jobData));
		logger.info("callBackRemind end" + response);
		return response;
	}

	/**
	 * 百可录通知类电话回调请求Api形式
	 * @param request
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "/baikelu/callBack/remindApi" ,method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public String callBackRemindApi(HttpServletRequest request) throws Exception {
		logger.info("callBackRemindApi start");
		String jobData="";
		String jobRef="";
		String response="";
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> fileItems = upload.parseRequest(request);
			for (FileItem fi : fileItems) {
				if (fi.isFormField() && fi.getFieldName().equals("work_id")) {
					jobRef=fi.getString("UTF-8");
					continue;
				}
				if (fi.isFormField() && fi.getFieldName().equals("work_result")) {
					jobData = fi.getString("UTF-8");
					continue;
				}
			}
			response=baikeluRemindService.baikeluCallbackRemindApi(jobRef,jobData);
		}catch (Exception  e){
			e.printStackTrace();
			logger.info("callBackRemind接口异常:",e);
			response= baikeluRemindService.baikeluCallBackFailApi(BaikeluRemindEnum.FAIL_ONE.getCode(),e.getMessage());
		}
		response= StringEscapeUtils.unescapeJava(response);
		logger.info("callBackRemindApi end" + response);
		return response;
	}

//    @RequestMapping(value = "/baikelu/callBack/remind/test" ,method = RequestMethod.GET)
//    @ResponseBody
//    public String callBackRemindTest(HttpServletRequest request) throws Exception {
//        logger.info("callBackRemind start");
//        String json="{\"biocloo_response\":{\"job_result\":\"部分接听\",\"phone_number\":\"17717374735\",\"job_result_code\":\"partial_passed\",\"calling_list\":[{\"calling_start_time\":\"2018-06-05 12:34:54\",\"calling_status\":\"部分接听\",\"calling_status_code\":\"partial_passed\",\"calling_duration\":\"6.82\"}],\"job_ref\":\"f67eb04a-2f28-4334-8ac2-0e5db23e8442\"}}";
//        String response= JSONObject.toJSONString(baikeluRemindService.baikeluCallbackRemind(json));
//        logger.info("callBackRemind end" + response);
//        return response;
//    }

	public static void main(String arge[]) throws Exception {
		/*String url = "http://localhost:8092/zloan-manage/robot/rcOrder.action";
		Map map = new HashMap<>();
		map.put("borrId","123");
		HttpUtils.sendPost(url, HttpUtils.toParam(map));*/
//		HttpUtils.sendPost(url, JSONObject.toJSONString(map),"x-www-form-urlencoded");
	}


}
