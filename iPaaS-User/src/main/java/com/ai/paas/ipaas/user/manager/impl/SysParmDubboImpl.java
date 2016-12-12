package com.ai.paas.ipaas.user.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.manage.rest.interfaces.ISysParamDubbo;
import com.ai.paas.ipaas.user.service.ISysParamSv;
import com.ai.paas.ipaas.vo.user.SysParamVo;
import com.ai.paas.ipaas.vo.user.SysParmRequest;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class SysParmDubboImpl implements ISysParamDubbo {
	
	@Autowired
	private ISysParamSv iSysParmSv;

	@Override
	public List<SysParamVo> getSysParams(SysParmRequest request) {		
		List<SysParamVo>  sysParamList = iSysParmSv.getSysParams(request);
		return sysParamList;
	}

	@Override
	public SysParamVo getSysParamsVo(SysParmRequest request) throws PaasException  {
		
		return iSysParmSv.getSysParamsVo(request);
	}

}
