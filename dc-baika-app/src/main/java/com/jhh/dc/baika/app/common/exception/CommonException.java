package com.jhh.dc.baika.app.common.exception;

public class CommonException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int resultCode;

	public CommonException(int resultCode, String resultMessage) {
		super(resultMessage);
		this.resultCode = resultCode;

	}

	public int getResultCode() {
		return resultCode;
	}

}
