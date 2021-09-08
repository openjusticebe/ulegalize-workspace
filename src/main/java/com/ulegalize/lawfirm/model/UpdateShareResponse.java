package com.ulegalize.lawfirm.model;

import lombok.Data;

import java.util.List;

@Data
public class UpdateShareResponse {

	private String yearDoss;
	private Long numDoss;
	private List<String> emails;
}
