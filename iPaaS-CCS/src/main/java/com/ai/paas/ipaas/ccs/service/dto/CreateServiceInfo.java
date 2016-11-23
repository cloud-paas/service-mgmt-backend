package com.ai.paas.ipaas.ccs.service.dto;

import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.ResourceUtil;

@SuppressWarnings("serial")
public class CreateServiceInfo extends ApplyInfo {

    private int timeOut;

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        return "CreateServiceInfo{" +
                "userId='" + getUserId() + '\'' +
                "orgCode='" + getOrgCode() + '\'' +
                ", applyType='" + getApplyType() + '\'' +
                ", serviceId='" + getServiceId() + '\'' +
                ", timeOut=" + timeOut +
                '}';
    }

    public void validate() {
        Assert.notNull(getUserId(), ResourceUtil.getMessage(BundleKeyConstants.USER_ID_NOT_NULL));
        Assert.notNull(getApplyType(), ResourceUtil.getMessage(BundleKeyConstants.APPLY_TYPE_NOT_NULL));
        Assert.notNull(getServiceId(), ResourceUtil.getMessage(BundleKeyConstants.SERVICE_ID_NOT_NULL));
        if (timeOut == 0){
            throw new IllegalArgumentException(ResourceUtil.getMessage(BundleKeyConstants.TIMEOUT_NOT_NULL));
        }
    }

}
