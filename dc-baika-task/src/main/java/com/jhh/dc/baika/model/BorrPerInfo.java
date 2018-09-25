package com.jhh.dc.baika.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter @Setter
public class BorrPerInfo implements Serializable {
	private Integer id;
	private Integer perId;
	private String name;
	private String surplusAmount;
	private String phone;
	private String payDate;
	private Date repayDate;
}
