package com.jhh.dc.baika.manage.task;

import com.jhh.dc.baika.manage.service.borr.BorrListService;
import com.jhh.dc.baika.manage.service.user.MasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserTask {
	private Logger logger = LoggerFactory.getLogger(UserTask.class);

	@Autowired
	private BorrListService borrListService;
	@Autowired
	private MasterService masterService;

//	@Scheduled(cron = "0 0 20 * * ? ") // 晚上八点开始
//	public void rejectAudit() {
//		if(masterService.isMaster()) {
//			logger.info("rejectAudit start");
//			borrListService.rejectAudit();
//		}
//	}
	/**
	 * 机器人上午打电话
	 * 九点给昨天签约的人打首单审核电话
	 */
	//@Scheduled(cron = "0 0 9 * * ? ")
	/*public void rcCallPhone() {
		if(masterService.isMaster()) {
			logger.info("rcCallPhone start");
			borrListService.rcCallPhone();
		}
	}*/

	/**
	 * 分单特殊
	 */
//	@Scheduled(cron = "0 0 5 * * ? ") // 早上5点开始
//	@Scheduled(cron = "0 0/1 * * * ? ")
//	public void submenuTransfer() {
//		if(masterService.isMaster()) {
//			logger.info("submenuTransfer start");
//			borrListService.submenuTransfer();
//		}
//	}

}
