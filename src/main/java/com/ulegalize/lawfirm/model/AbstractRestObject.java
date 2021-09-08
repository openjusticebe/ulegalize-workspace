package com.ulegalize.lawfirm.model;

import java.util.Date;

public abstract class AbstractRestObject {
	
	
	private Date creationDate;
	private Date updateDate;
	
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	

}
