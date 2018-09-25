package com.jhh.dc.baika.service.impl;


import com.jhh.dc.baika.model.BorrPerInfo;
import com.jhh.dc.baika.util.DateUtil;
import com.jhh.dc.baika.api.entity.ResponseDo;
import com.jhh.dc.baika.api.sms.SmsService;
import com.jhh.dc.baika.dao.BorrowListMapper;
import com.jhh.dc.baika.model.BillingNotice;
import com.jhh.dc.baika.service.TimerService;
import com.jhh.dc.baika.service.UserService;
import com.jhh.dc.baika.util.Detect;
import com.jhh.dc.baika.util.ExcelUtils;
import com.jhh.dc.baika.util.MailSender;
import com.jhh.dc.baika.common.enums.SmsTemplateEnum;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("timerService") @Getter @Setter
public class TimerServiceImpl implements TimerService {
	private static final String HSSF  = ".xls";
	private static final String XSSF  = ".xlsx";

	private static final Logger logger = LoggerFactory.getLogger(TimerServiceImpl.class);

	@Value("${system.isTest}")
	private String isTest;
	@Value("${mail.wzz.host}")
	private  String host ;
	@Value("${mail.wzz.name}")
	private  String name ;
	@Value("${mail.wzz.pwd}")
	private  String pwd ;
	@Value("${mail.wzz.from}")
	private  String from ;
	@Value("${mail.wzz.filePath}")
	private  String filePath ;
	@Value("${filePath.moneyManagement}")
	private String moneyManagementFilePath;
	@Value("${sms.message}")
	private String smsMessage;

	private static String[] to = {"xuyaozong@jinhuhang.com.cn","yangyan@jinhuhang.com.cn"};
	private static String[] copyto = {"chenzhen@jinhuhang.com.cn","luoqian@jinhuhang.com.cn","liuhongming@jinhuhang.com.cn","2439357@qq.com"};

	@Autowired
	private BorrowListMapper borrMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private SmsService smsService;

	@Override
	public void smsAlert() {
		logger.info("进入还款提醒的定时任务---------------------------------------");
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		// 这个时间就是日期往后推一天的结果
		String dd2 = DateUtil.getDateString(calendar.getTime());
		String da1 = DateUtil.getDateString(new Date());
		List<BorrPerInfo> borrPerInfos = borrMapper.getMingtianhuankuanId(da1,dd2);
		if (Detect.notEmpty(borrPerInfos)) {
			List<BillingNotice> list = new LinkedList();
			for (BorrPerInfo borrPerInfo : borrPerInfos) {
				try{
					// 发送站内信
					String params=borrPerInfo.getName() + "," + borrPerInfo.getSurplusAmount() + "," + DateUtil.getDateString(borrPerInfo.getRepayDate());
					String result = userService.setMessage(borrPerInfo.getPerId() + "", "4", params);

					JSONObject obje = JSONObject.fromObject(result);
					if ("200".equals(obje.get("code").toString())) {
						logger.info("消息发送成功！");
						// 发送短信

						//String remessage ="【悠兔白卡】尊敬的" +
						//		borrPerInfo.getName() +"，到当期为止您应付租金" +
						//		borrPerInfo.getSurplusAmount() +"元。当期到期日为" +
						//		DateUtil.getDateString(borrPerInfo.getRepayDate());

						BillingNotice bn = null;
						ResponseDo vo=smsService.sendSms(SmsTemplateEnum.RENT_REMIND.getCode(),borrPerInfo.getPhone(),borrPerInfo.getName(),borrPerInfo.getSurplusAmount(),DateUtil.getDateString(borrPerInfo.getRepayDate()));
						//String resultWuXun = WuXunSmsUtil.sendResult(remessage, borrPerInfo.getPhone(), 1);
						if(isTest.equals("off")){
							bn = new BillingNotice(borrPerInfo.getPhone(), vo.getCode() + "");
						}else{
							bn = new BillingNotice(borrPerInfo.getPhone(), "200");
						}
						list.add(bn);
					} else {
						logger.info(obje.get("info").toString());
					}
				}catch(Exception e){
					logger.error(e.getMessage());
				}
			}
			
			//生成Excel
			String fileName = "phone_expire_repay" + DateUtil.getDateStringyyyymmdd(new Date());
			fileName = createExcel(list, filePath, fileName, BillingNotice.class);
			//发送邮件
			logger.info("发送邮件");
			if("on".equals(isTest)){
				to = new String[]{"wanzezhong@jinhuhang.com.cn"};
				copyto =  new String[]{"chenzhen@jinhuhang.com.cn","luoqian@jinhuhang.com.cn","wangge@jinhuhang.com.cn"};
			}
			this.sendMail(from, to, copyto, filePath + fileName, fileName,"【悠兔白卡】逾期提醒" ,"【悠兔白卡】号码推送");
		}
	}

	@Override
	public void smsOverdue() {
		List<BorrPerInfo> borrPerInfos = borrMapper.getOverdueData(7);

		for(BorrPerInfo perInfo:borrPerInfos){
			//String msg = "【悠兔白卡】尊敬的" + perInfo.getName() + "，您已违约逾期7天，" +
			//		"请于今日付清截至当期租金及违约金，否则明天将强制要求您结清剩余全部费用。" +
			//		"感谢您的配合!如有疑虑请详询：021-60550260";
			// 发送站内信
			userService.setMessage(perInfo.getPerId() + "", "6", perInfo.getName());
			//发送短信
			smsService.overdueSms(SmsTemplateEnum.OVERDUE_REMIND.getCode(),perInfo.getPhone(),perInfo.getName());
			//WuXunSmsUtil.send(msg, perInfo.getPhone(), 2);
		}
	}

	private void sendMail(String from, String[] to, String[] copyto, String filePath, String fileName, String mainTitle, String mailContent){
		MailSender cn = new MailSender();
		// 设置发件人地址、收件人地址和邮件标题
 		cn.setAddress(from, to, copyto, mainTitle, mailContent);
		// 设置要发送附件的位置和标题
		cn.setAffix(filePath, fileName);
		// 设置smtp服务器以及邮箱的帐号和密码
		cn.send(host, name, pwd);
	}

	private void sendMailArray(String from, String[] to, String[] copyto, String[] filePathArray, String[] fileNameArray, String mainTitle, String mailContent){
		MailSender cn = new MailSender();
		// 设置发件人地址、收件人地址和邮件标题
		cn.setAddress(from, to, copyto, mainTitle, mailContent);
		// 设置要发送附件的位置和标题
		cn.setAffixArray(filePathArray, fileNameArray);
		// 设置smtp服务器以及邮箱的帐号和密码
		cn.send(host, name, pwd);
	}

	private String createExcel(List result,String path, String fileName, Class excilClass){
		Map<String, Object> excilMap = new HashMap<String, Object>();
		excilMap.put(NormalExcelConstants.FILE_NAME, fileName);
		excilMap.put(NormalExcelConstants.CLASS, excilClass);

		excilMap.put(NormalExcelConstants.DATA_LIST, result);
		ExportParams exportParams = new ExportParams();
		exportParams.setType(ExcelType.XSSF);
		excilMap.put(NormalExcelConstants.PARAMS, exportParams);
		Workbook workbook = ExcelUtils.getWorkbook(excilMap);

		//保存Excel
		if (workbook instanceof HSSFWorkbook) {
			fileName += HSSF;
		} else {
			fileName += XSSF;
		}
		logger.info("生成excel");
		ExcelUtils.saveFile(workbook, path, fileName);
		return fileName;
	}


	public static void main(String[] arge){
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, 2);// 把日期往后增加一天.整数往后推,负数往前移动
		// 这个时间就是日期往后推一天的结果
		String dd = DateUtil.getDateString(calendar.getTime());
		String da1 = DateUtil.getDateString(new Date());
		System.out.println(dd);
		System.out.println(da1);
	}

}
