package com.ai.paas.ipaas.ses.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ses.manage.rest.interfaces.IRPCSesUserInst;
import com.ai.paas.ipaas.ses.service.ISesUserInst;
import com.ai.paas.ipaas.util.CloneTool;
import com.ai.paas.ipaas.vo.ses.SesUserInstance;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class RPCSesUserInstImpl implements IRPCSesUserInst {

	@Autowired
	ISesUserInst sesUserInst;

	@Override
	public SesUserInstance queryInst(String userId, String srvId)
			throws PaasException {
		com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserInstance userInst = sesUserInst
				.queryInst(userId, srvId);
		return CloneTool.clone(userInst, SesUserInstance.class);
	}

}
