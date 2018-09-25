package com.jhh.dc.baika.manage.service.report;

import java.io.IOException;

import com.jhh.dc.baika.manage.entity.Response;

public interface ReportService {

	/**
	 * 获取聚信立报告页面
	 * @return
	 */
	Response getPolyXinliReport();

	/**
	 * 获取通讯录
	 * @param perId
	 * @param phones
	 * @param offset
	 * @param size
	 * @return
	 */
	Response getContact(Integer perId,String phones, Integer offset,Integer size) throws IOException;

	/**
	 * 导出通讯录信息查询
	 * @param perId
	 * @return
	 */
	Response getContactForExport(Integer perId);
}
