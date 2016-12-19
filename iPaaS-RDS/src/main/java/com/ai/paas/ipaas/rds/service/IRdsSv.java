package com.ai.paas.ipaas.rds.service;

import com.ai.paas.ipaas.PaasException;

public interface IRdsSv {
	public String create(String stopApply) throws PaasException;
	public String createslobm(String stopApply) throws PaasException;
	public String cancel(String cancelApply) throws PaasException;
	public String start(String startApply) throws PaasException;
	public String stop(String stopApply) throws PaasException;
	
	public String getFuncList() throws PaasException;
	public String restart(String restartApply) throws PaasException;
	public String getInstanceInfo(String instanceApply) throws PaasException;
	public String modify(String modifyApply) throws PaasException;
}
