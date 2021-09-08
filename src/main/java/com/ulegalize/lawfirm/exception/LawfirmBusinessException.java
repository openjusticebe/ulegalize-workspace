package com.ulegalize.lawfirm.exception;

public class LawfirmBusinessException extends Exception {

	public LawfirmBusinessException() {
		super();
	}
	
	public LawfirmBusinessException(String error) {
		super(error);
	}
}
