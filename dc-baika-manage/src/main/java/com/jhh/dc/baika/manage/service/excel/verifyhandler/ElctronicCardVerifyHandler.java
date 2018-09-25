package com.jhh.dc.baika.manage.service.excel.verifyhandler;

import com.jhh.dc.baika.manage.service.excel.ImportJDCardExcelEntity;
import com.jhh.dc.baika.common.util.Detect;
import org.jeecgframework.poi.excel.entity.result.ExcelVerifyHanlderResult;
import org.jeecgframework.poi.handler.inter.IExcelVerifyHandler;
import org.springframework.stereotype.Component;

@Component
public class ElctronicCardVerifyHandler implements IExcelVerifyHandler<ImportJDCardExcelEntity> {
	
	@Override
	public ExcelVerifyHanlderResult verifyHandler(ImportJDCardExcelEntity obj) {
		StringBuffer builder = new StringBuffer();
		if(!Detect.notEmpty(obj.getCardNumber())){
			builder.append("卡号不能为空");
		}

		if(!Detect.notEmpty(obj.getPassword())){
			builder.append("卡密不能为空;");
		}

		if(null == obj.getStartDate()){
			builder.append("有效开始日期不能为空;");
		}

		if(null == obj.getEndDate()){
			builder.append("有效结束日期不能为空;");
		}
		
		if(Detect.notEmpty(builder.toString())){
			return new ExcelVerifyHanlderResult(false, builder.toString());
		}

//		obj.setStatus(1);//上传的产品默认待审核状态
		return new ExcelVerifyHanlderResult(true);
	}
}
