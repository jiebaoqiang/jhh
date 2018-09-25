package com.jhh.dc.baika.entity.manager_vo;

import java.io.Serializable;

import com.jhh.dc.baika.entity.manager.Feedback;

public class FeedbackVo extends Feedback implements Serializable{
	private String phone;
	private String productName;

	public String getProdId() {
		return productName;
	}

	public void setProdId(String prodId) {
		this.productName = prodId;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
  
}