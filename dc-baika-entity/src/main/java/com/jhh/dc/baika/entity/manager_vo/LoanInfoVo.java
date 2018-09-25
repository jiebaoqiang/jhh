package com.jhh.dc.baika.entity.manager_vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 
*描述：
*@author: Wanyan
*@date： 日期：2016年11月10日 时间：下午9:28:15
*@version 1.0
 */
@Setter
@Getter
public class LoanInfoVo  implements Serializable {
	private int id;
	private String borrNum;
	private String productName;
	private String borrAmount;
	private String loanDay;
	private String planrepayDate;
	private Date makeborrDate;
	private Date actRepayDate;
	private Date payDate;
	private String amount;
	private String planRepay;
	private String actRepayAmount;
	private String borrStatus;
	private String isManual;
	private String description;
	private String serial_number_url;
	private String serial_number;
	private String cardNum;
	private String prodType;
	private String jdNumber;
}
