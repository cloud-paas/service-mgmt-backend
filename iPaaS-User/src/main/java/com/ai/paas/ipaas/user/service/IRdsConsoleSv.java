package com.ai.paas.ipaas.user.service;

import java.util.List;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.dubbo.vo.ResponseHeader;
import com.ai.paas.ipaas.user.dubbo.vo.UserProdInstVo;

public interface IRdsConsoleSv {
	public List<UserProdInstVo> selectUserProdInsts(UserProdInstVo vo)  throws PaasException;

	public ResponseHeader stopRdsContainer(String paraprodBackPara)   throws PaasException;
	
	public ResponseHeader startRdsContainer(String paraprodBackPara)   throws PaasException;
	
	public ResponseHeader destroyContainer(String paraprodBackPara)   throws PaasException;
	
	public List<UserProdInstVo> selectUserProdInstById(UserProdInstVo vo)  throws PaasException;
}
