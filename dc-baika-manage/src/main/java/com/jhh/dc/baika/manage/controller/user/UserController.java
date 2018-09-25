package com.jhh.dc.baika.manage.controller.user;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jhh.dc.baika.common.util.DateUtil;
import com.jhh.dc.baika.common.util.ExcelUtils;
import com.jhh.dc.baika.manage.controller.BaseController;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.entity.Result;
import com.jhh.dc.baika.manage.service.loan.SystemUserService;
import com.jhh.dc.baika.manage.service.user.UserService;
import com.jhh.dc.baika.manage.utils.QueryParamUtils;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.entity.manager.UserList;
import com.jhh.dc.baika.manage.mapper.CodeValueMapper;

import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private SystemUserService collectorsService;

	@Autowired
	private UserService userService;

	@Autowired
	private CodeValueMapper codeValueMapper;

	@ResponseBody
	@RequestMapping(value ="/addOfflineUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public Result addOfflineUser(String phone) {
		return userService.addOfflineUser(phone);
	}

	@ResponseBody
	@RequestMapping(value ="/userLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public Result userLogin(String loginname, String loginpassword, Integer source, String loginVerifyCode,HttpServletRequest req) {
		Result result = new Result();
		try {
			result = collectorsService.loadLoginUser(loginname, loginpassword,source,loginVerifyCode);

		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Result.FAIL);
			result.setMessage("账号校验失败，账户不存在.");
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value ="/userAuthInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST )
	public Result userAuthInfo(String userAuth) {
		Result result = new Result();
		try {
			result = collectorsService.loadUserAuthInfo(userAuth);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Result.FAIL);
			result.setMessage("账号校验失败，账户不存在.");
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value ="/userRoleInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST )
	public Result userRoleInfo(String userCategory) {
		Result result = new Result();
		try {
			result = collectorsService.loadUserRoleInfo(userCategory);
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(Result.FAIL);
			result.setMessage("账号校验失败，账户不存在.");
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value ="/checkLoginName",  method = RequestMethod.POST )
	public Result checkLoginName(String loginname) {
		Result result = new Result();
		try {
			result = collectorsService.checkLoginName(loginname);
		} catch (Exception e) {
			result.setCode(Result.FAIL);
			result.setMessage("账号校验失败，账户不存在.");
		}
		return result;
	}

	/**
	 * 获取用户详细信息
	 * @param perId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="/person/{perId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
	public Response getUserInfo(@PathVariable Integer perId) {
		return userService.getUserInfo(perId);
	}
	/**
	 * 获取身份证
	 * @param perId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/identityCard/{perId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getIdentityCard(@PathVariable Integer perId) {
		return userService.getIdentityCard(perId);
	}

	/**
	 * 拉黑
	 * @param himid_list
	 * @param usernum
	 * @param operator
	 * @param reason
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/black", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response personForBlack(Integer himid_list, String usernum, String operator, String reason,Integer type) throws Exception {
		Integer tp;
		if(type.equals(Constants.UserBlockWhite.WHITE)){
			tp = Constants.UserBlockWhite.WHITE;
		}else if(type.equals(Constants.UserBlockWhite.BLACK)){
			tp = Constants.UserBlockWhite.BLACK;
		}else{
			return new Response().msg("拉黑参数类型错误");
		}
		return userService.userBlockWhite(himid_list, usernum, operator, reason, tp);
	}

	/**
	 * 黑名单列表
	 * @param perId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/black/{perId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getBlackList(@PathVariable Integer perId) throws Exception {
		return userService.getBlackList(perId);
	}

	/**
	 * 银行卡列表
	 * @param perId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/bank/{perId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getBankList(@PathVariable Integer perId) throws Exception {
		return userService.getBankList(perId);
	}

	/**
	 * 有效银行卡列表
	 * @param perId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/validBank/{perId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getValidBankList(@PathVariable Integer perId) throws Exception {
		return userService.getValidBankList(perId);
	}

	/**
	 * 流水列表
	 * @param perId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/order/{perId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getOrderList(@PathVariable Integer perId) throws Exception {
		return userService.getOrderList(perId);
	}

	/**
	 * 节点列表
	 * @param perId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/node/{perId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getNodeList(@PathVariable Integer perId) throws Exception {
		return userService.getNodeList(perId);
	}

	/**
	 * 节点详情列表
	 * @param perId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/node/detail/{perId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response getNodeDetailList(@PathVariable Integer perId) throws Exception {
		return userService.getNodeDetailList(perId);
	}
	/**
	 * 用戶列表查詢
	 * */
	@ResponseBody
	@RequestMapping(value = "/getusers", method = RequestMethod.POST)
	public String getusers(HttpServletRequest request) {
		List<Map> list = getUserList(request);
		return JSON.toJSONString(new PageInfo(list));
	}

	@ResponseBody
	@RequestMapping(value = "/getChannelUsers", method = RequestMethod.POST)
	public String getChannelusers(HttpServletRequest request) {

		List<Map> list = getChannelUserList(request);
		return JSON.toJSONString(new PageInfo(list));
	}

	@ResponseBody
	@RequestMapping(value = "/source",method = RequestMethod.GET)
	public Result getSource(@RequestParam  String code){
		Result result = new Result();
		try {
			List list = userService.getSource(code);
			result.setCode(1);
			result.setObject(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("加载渠道来源报错:{}",e.getMessage());
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 用户列表结果导出
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list/export", method = RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response) {
		QueryParamUtils.buildPage(request, 50000);
		List<Map> list = getUserList(request);
		list.forEach(m ->{
			String source = (String) m.get("source_name");
			m.put("source_name",codeValueMapper.getMeaningByTypeCode("register_source",source));
		});
		Map <String, Object> map = new HashMap<>();
		map.put(NormalExcelConstants.FILE_NAME, DateUtil.getDateTimeString(Calendar.getInstance().getTime()));
		map.put(NormalExcelConstants.CLASS, UserList.class);
		map.put(NormalExcelConstants.DATA_LIST,JSON.parseArray(JSON.toJSONString(list), UserList.class));
		map.put(NormalExcelConstants.PARAMS, new ExportParams());
		ExcelUtils.jeecgSingleExcel(map, request,response);
	}

	/**
	 * 节点列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getRiskNodeAndStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map getRiskNodeAndStatus() throws Exception {
		return userService.getRiskNodeAndStatus();
	}

	/**
	 *  获取用户管理列表
	 * @param request
	 * @return
     */
	private List<Map> getUserList(HttpServletRequest request){
		request.getParameterMap();
		int offset =  Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
		int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
		PageHelper.offsetPage(offset, size);
		Response rsp ;
		List<Map> list = null;
		try {
			rsp = userService.getUsers(request.getParameterMap());
			list = (List)rsp.getData();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询用户列表失败:{}",e.getMessage());
		}
		return list;
	}

	private List<Map> getChannelUserList(HttpServletRequest request){
		request.getParameterMap();
		int offset =  Integer.valueOf(request.getParameter("skip") == null ? "0" : request.getParameter("skip"));
		int size = Integer.valueOf(request.getParameter("take") == null ? Integer.MAX_VALUE + "" : request.getParameter("take"));
		String userId = request.getParameter("userId");

		Response rsp ;
		List<Map> list = null;
		try {
			Map map = new HashMap();
			map.putAll(request.getParameterMap());
			//查询登录人员的渠道商的编号
			String channelSource = collectorsService.getChannelSource(userId);
			//确保pageHelper放在后面
			PageHelper.offsetPage(offset, size);
			String[] sourceArr = new String[]{channelSource};
			map.put("source",sourceArr);
			rsp = userService.getChannelUsers(map);
			list = (List)rsp.getData();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询渠道用户列表失败:{}",e.getMessage());
		}
		return list;
	}
}
