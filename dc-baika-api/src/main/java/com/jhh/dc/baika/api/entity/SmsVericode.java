package com.jhh.dc.baika.api.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * SmsVericode entity. @author MyEclipse Persistence Tools
 */
@Setter
@Getter
@ToString
public class SmsVericode implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int count;
	private Long id;
	private String vericode;
	private Date createTime;
	private int version;
	private String transType;
	private Long transSeq;
	private String phoneNo;
	// Constructors
	public SmsVericode(String transType, Long transSeq, String phoneNo) {
		this.transType = transType;
		this.transSeq = transSeq;
		this.phoneNo = phoneNo;
	}
	public SmsVericode() {
	}


}
