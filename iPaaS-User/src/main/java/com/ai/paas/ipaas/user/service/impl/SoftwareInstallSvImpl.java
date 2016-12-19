package com.ai.paas.ipaas.user.service.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.dto.OrderDetail;
import com.ai.paas.ipaas.user.dto.ProdProduct;
import com.ai.paas.ipaas.user.dto.UserMessage;
import com.ai.paas.ipaas.user.service.ISoftwareInstallSv;
import com.ai.paas.ipaas.user.service.dao.OrderDetailMapper;
import com.ai.paas.ipaas.user.service.dao.ProdProductMapper;
import com.ai.paas.ipaas.user.service.dao.UserMessageMapper;
import com.ai.paas.ipaas.user.utils.DateUtil;

/**
 * 软件安装提交后场服务
 */
@Transactional
@Service
public class SoftwareInstallSvImpl implements ISoftwareInstallSv {
	@Autowired
	private SqlSessionTemplate template;

	@Override
	public String softwareInstallSubmit(String params) throws Exception {
		/** 屏蔽portal中的iaas模块功能，屏蔽此方法路逻辑。2016-06-12 **/
		return null;
	}

	public void saveUserMessage(String orderDetailId) throws PaasException {
		OrderDetailMapper orderDetailMapper = template.getMapper(OrderDetailMapper.class);
		OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(Long.parseLong(orderDetailId));
		ProdProductMapper prodProductMapper = template.getMapper(ProdProductMapper.class);
		ProdProduct prodProduct = prodProductMapper.selectByPrimaryKey(Short.parseShort(orderDetail.getProdId()));
		if (prodProduct == null) {
			throw new PaasException("根据id查询产品信息为空");
		}
		String content = "您订购的" + prodProduct.getProdName() + " 软件安装成功。";

		UserMessage userMessage = new UserMessage();
		userMessage.setUserId(orderDetail.getUserId());
		userMessage.setUserMsgContent(content);
		userMessage.setUserMsgSendTime(DateUtil.getSysDate());
		UserMessageMapper userMessageMapper = template.getMapper(UserMessageMapper.class);
		userMessageMapper.insert(userMessage);
	}
}
