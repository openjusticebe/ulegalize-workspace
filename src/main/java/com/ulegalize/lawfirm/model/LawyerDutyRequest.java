package com.ulegalize.lawfirm.model;

import lombok.Data;

@Data
public class LawyerDutyRequest extends LawyerDuty {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	
	private String note;

}
