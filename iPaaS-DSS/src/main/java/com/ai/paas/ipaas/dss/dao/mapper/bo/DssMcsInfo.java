package com.ai.paas.ipaas.dss.dao.mapper.bo;

public class DssMcsInfo {
    private Integer id;

    private String mcsAddress;

    private Integer status;

    private String orgCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMcsAddress() {
        return mcsAddress;
    }

    public void setMcsAddress(String mcsAddress) {
        this.mcsAddress = mcsAddress == null ? null : mcsAddress.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }
}