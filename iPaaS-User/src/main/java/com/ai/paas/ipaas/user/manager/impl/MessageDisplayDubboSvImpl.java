package com.ai.paas.ipaas.user.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.constants.Constants;
import com.ai.paas.ipaas.user.manage.rest.interfaces.MessageDisplayDubboSv;
import com.ai.paas.ipaas.user.service.MessageDisplaySv;
import com.ai.paas.ipaas.vo.user.PageResult;
import com.ai.paas.ipaas.vo.user.ResponseHeader;
import com.ai.paas.ipaas.vo.user.UserMessageRequest;
import com.ai.paas.ipaas.vo.user.UserMessageResponse;
import com.ai.paas.ipaas.vo.user.UserMessageVo;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class MessageDisplayDubboSvImpl implements MessageDisplayDubboSv{
	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private MessageDisplaySv messageDisplaySv;

	@Override
	public UserMessageResponse searchPage(UserMessageRequest request) throws PaasException{
		
		ResponseHeader responseHeader = new ResponseHeader();	
		PageResult<UserMessageVo>   pageResult  = new PageResult<UserMessageVo>();
		try{
			pageResult = messageDisplaySv.searchPage(request);
			responseHeader.setResultCode(Constants.OPERATE_CODE_SUCCESS);
		}catch(Exception e){
			responseHeader.setResultCode(Constants.OPERATE_CODE_FAIL);
			e.printStackTrace();
			logger.error(e.getMessage());
		}		
		UserMessageResponse response = new UserMessageResponse();
		response.setPageResult(pageResult);
		response.setResponseHeader(responseHeader);		
		return response;
	}
	
	
	

}
