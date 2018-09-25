package com.jhh.dc.baika.manage.service.excel;

import com.jhh.id.annotation.IdGenerator;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用于导入电子卡的实体类
 * @author carl.wan
 * 2018年7月24日 15:30:59
 */
@Setter @Getter
public class ImportJDCardExcelEntity {
    @Id
    @IdGenerator(value = "dc_baika_jd_card_info")
	private long id;
	
    @Excel(name = "卡号")
    private String cardNumber;
    
    @Excel(name = "卡密")
    private String password;
    
    @Excel(name = "有效开始日期",format="yyyy-MM-dd")
    private Date startDate;
    
    @Excel(name = "有效结束日期",format="yyyy-MM-dd")
    private Date endDate;

	private Date createDate;

	private Date updateDate;


}
