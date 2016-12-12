package com.ai.paas.ipaas.user.service;

import com.ai.paas.ipaas.vo.user.PageResult;
import com.ai.paas.ipaas.vo.user.UserMessageRequest;
import com.ai.paas.ipaas.vo.user.UserMessageVo;

public interface MessageDisplaySv{
	     public abstract PageResult<UserMessageVo> searchPage(UserMessageRequest request);
}
