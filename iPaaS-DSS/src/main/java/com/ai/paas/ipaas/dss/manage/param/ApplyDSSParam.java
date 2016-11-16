package com.ai.paas.ipaas.dss.manage.param;

import java.io.Serializable;

import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;



public class ApplyDSSParam extends ApplyInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5663188213246570830L;

	private String capacity;
	private String singleFileSize;
	private String serviceName;
	private int orgId;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getSingleFileSize() {
		return singleFileSize;
	}

	public void setSingleFileSize(String singleFileSize) {
		this.singleFileSize = singleFileSize;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

}
