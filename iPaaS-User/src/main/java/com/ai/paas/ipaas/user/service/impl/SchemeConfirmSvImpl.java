package com.ai.paas.ipaas.user.service.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.constants.Constants;
import com.ai.paas.ipaas.user.dto.OrderDetail;
import com.ai.paas.ipaas.user.dto.ProdProduct;
import com.ai.paas.ipaas.user.dto.UserMessage;
import com.ai.paas.ipaas.user.service.ISchemeConfirmSv;
import com.ai.paas.ipaas.user.service.dao.ProdProductMapper;
import com.ai.paas.ipaas.user.service.dao.UserMessageMapper;
import com.ai.paas.ipaas.user.utils.DateUtil;

@Service
public class SchemeConfirmSvImpl implements ISchemeConfirmSv {
	@Autowired
	private SqlSessionTemplate template;

	@Override
	public String schemeSubmit(String params) throws Exception {
		/** 屏蔽portal中的iaas模块功能，屏蔽此方法路逻辑。2016-06-12 **/
		return null;
	}

	public void saveUserMessage(OrderDetail orderDetail) throws PaasException {
		// OrderDetail orderDetail =
		// this.selectByPrimaryKey(orderDetailTmp.getOrderDetailId());
		ProdProductMapper prodProductMapper = template.getMapper(ProdProductMapper.class);
		ProdProduct prodProduct = prodProductMapper.selectByPrimaryKey(Short.parseShort(orderDetail.getProdId()));
		if (prodProduct == null) {
			throw new PaasException("根据id查询产品信息为空");
		}
		String content = "";
		if (Constants.Order.OrderCheckResult.CHECK_PASS.equals(orderDetail.getOrderCheckResult())) {
			content = "您订购的" + prodProduct.getProdName() + "审核通过";
		} else {

			if (orderDetail.getOrderCheckDesc() != null) {
				content = "您订购的" + prodProduct.getProdName() + "审核不通过通过，原因：" + orderDetail.getOrderCheckDesc();
			} else {
				content = "您订购的" + prodProduct.getProdName() + "审核不通过通过，原因：OA审核不通过！";
			}
		}
		UserMessage userMessage = new UserMessage();
		userMessage.setUserId(orderDetail.getUserId());
		userMessage.setUserMsgContent(content);
		userMessage.setUserMsgSendTime(DateUtil.getSysDate());
		UserMessageMapper userMessageMapper = template.getMapper(UserMessageMapper.class);
		userMessageMapper.insert(userMessage);
	}

}
