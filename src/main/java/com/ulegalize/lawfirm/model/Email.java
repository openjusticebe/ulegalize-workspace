package com.ulegalize.lawfirm.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Email {

	private String from;
	private List<String> to = new ArrayList<String>();
	private String subject;
	private String body;
}
