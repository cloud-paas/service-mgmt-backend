package com.ai.paas.ipaas.user.service;

import java.util.List;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.vo.user.UserProdInstVo;

public interface ICcsConsoleSv {
	public List<UserProdInstVo> selectUserProdInsts(UserProdInstVo vo)  throws PaasException;

	public String cancleUserProdInst(UserProdInstVo vo) throws PaasException;
}
