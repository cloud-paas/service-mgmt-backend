package com.ai.paas.ipaas.user.service;

import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.dto.UserProdInst;
import com.ai.paas.ipaas.vo.user.UserProdInstVo;

public interface IUserProdInstSv {
	public List<UserProdInstVo> selectUserProdInsts(Map<String,String>  map)  throws PaasException;	
	
	public UserProdInst selectUserProdInstsById(Long userServId);
     
	public int updateUserprodInstById(UserProdInst userProdInst);
}
