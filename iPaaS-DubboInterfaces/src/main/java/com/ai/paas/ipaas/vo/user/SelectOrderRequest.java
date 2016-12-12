package com.ai.paas.ipaas.vo.user;

import java.io.Serializable;

public class SelectOrderRequest implements Serializable{

	private static final long serialVersionUID = 1L;	
	
	private PageEntity pageEntity;

	public PageEntity getPageEntity() {
		return pageEntity;
	}

	public void setPageEntity(PageEntity pageEntity) {
		this.pageEntity = pageEntity;
	}	
	
	

}
