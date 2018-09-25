package com.jhh.dc.baika.entity.manager_vo;

import java.io.Serializable;

import com.jhh.dc.baika.entity.manager.Review;

public class ReviewVo extends Review implements Serializable{

    private String employeeName;

    private String meaning;

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the meaning
	 */
	public String getMeaning() {
		return meaning;
	}

	/**
	 * @param meaning the meaning to set
	 */
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
    
    

}