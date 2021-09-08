package com.ulegalize.lawfirm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class LawyerDuty {

	private Long id;
	@Setter
	@Getter
	private Date start;
	@Setter
	@Getter
	private Date end;
	private String remark;

	@JsonProperty("lawyer_id")
	private Long lawyerId;

	public LawyerDuty() {

	}

	public LawyerDuty(Long id, Date start, Date end, String remark, Long lawyerId) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.remark = remark;
		this.lawyerId = lawyerId;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
