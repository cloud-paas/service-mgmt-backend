package com.ai.paas.ipaas.user.manager.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.dao.interfaces.OrgnizeUserInfoMapper;
import com.ai.paas.ipaas.user.dao.mapper.bo.OrgnizeUserInfo;
import com.ai.paas.ipaas.user.dao.mapper.bo.OrgnizeUserInfoCriteria;
import com.ai.paas.ipaas.user.manage.rest.interfaces.IOrgnizeUserInfoSv;
import com.ai.paas.ipaas.vo.user.OrgnizeUserInfoVo;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class OrgnizeUserInfoSvImpl implements IOrgnizeUserInfoSv{
	
	@Autowired
    private SqlSessionTemplate template;
	
	@Override
	public OrgnizeUserInfoVo getOrgnizeUserInfo(String userId) throws PaasException {
		OrgnizeUserInfoVo orguserinf = new OrgnizeUserInfoVo();
		OrgnizeUserInfoMapper mapper = template.getMapper(OrgnizeUserInfoMapper.class);
		OrgnizeUserInfoCriteria condition = new OrgnizeUserInfoCriteria();
		condition.createCriteria().andUserIdEqualTo(userId);
		List<OrgnizeUserInfo> beans = mapper.selectByExample(condition);
		//一个用户只能属于一个组织	
		if(beans.size()!=1){ 
			new PaasException("get orgnize user info error!");
		}
		BeanUtils.copyProperties(beans.get(0), orguserinf);
		return orguserinf;
	}

	@Override
	public void saveOrgnizeUserInfo(OrgnizeUserInfoVo orgnizeuser) throws PaasException {
		OrgnizeUserInfo orguserInfo = new OrgnizeUserInfo();
		BeanUtils.copyProperties(orgnizeuser, orguserInfo);
		OrgnizeUserInfoMapper mapper = template.getMapper(OrgnizeUserInfoMapper.class);
		mapper.insert(orguserInfo);		
	}
	
	@Override
	public int getOrgUsrInfoCntByOrgId(Integer orgId) throws PaasException {
		int OUcnt = 0;
		OrgnizeUserInfoMapper mapper = template.getMapper(OrgnizeUserInfoMapper.class);
		OrgnizeUserInfoCriteria condition = new OrgnizeUserInfoCriteria();
		condition.createCriteria().andOrgIdEqualTo(orgId);
		List<OrgnizeUserInfo> beans = mapper.selectByExample(condition);
		//一个用户只能属于一个组织	
		if(beans != null){ 
			OUcnt =  beans.size();
		}
		return OUcnt;
	}
	
	/*@Override
	public String getOrgCodeByUserId(String userId) {
		String orgCode = ServiceUtil.getMapper(
				IOrgnizeUserInfoSv.class).getOrgCodeByUserId(userId);
		return orgCode;
	}*/
}
