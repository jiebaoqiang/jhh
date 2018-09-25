package com.jhh.dc.baika.entity.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class BorrPerInfo implements Serializable {
	
	private String borrId;
	private String name;
	private double maximum_amount;
	private int term_value;
	private String planrepay_date;
	private double monthly_rate;
	private String phone;
	private String surplus_quota;
	private String surplus_penalty;
	private String surplus_penalty_Interes;
	private String prodType;
	private String totalTermNum;
	

	
	
}
