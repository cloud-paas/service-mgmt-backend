package com.ai.paas.ipaas.vo.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class MdsUserPageVo implements Serializable {

	private static final long serialVersionUID = -2111706726566522902L;

	private String userId;

	private String applyType;

	private String serviceId;


	private String resultCode;
	
	private String resultMsg;
	
	private List<MdsPartitionVo> topicUsage;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public List<MdsPartitionVo> getTopicUsage() {
		return topicUsage;
	}

	public void setTopicUsage(List<MdsPartitionVo> topicUsage) {
		this.topicUsage = topicUsage;
	}

}
