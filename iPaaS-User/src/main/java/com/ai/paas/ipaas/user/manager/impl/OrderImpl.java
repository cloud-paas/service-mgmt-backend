package com.ai.paas.ipaas.user.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.constants.Constants;
import com.ai.paas.ipaas.user.manage.rest.interfaces.IOrder;
import com.ai.paas.ipaas.user.service.IOrderSv;
import com.ai.paas.ipaas.util.JSonUtil;
import com.ai.paas.ipaas.vo.user.CheckOrdersRequest;
import com.ai.paas.ipaas.vo.user.OrderDetailRequest;
import com.ai.paas.ipaas.vo.user.OrderDetailResponse;
import com.ai.paas.ipaas.vo.user.OrderDetailVo;
import com.ai.paas.ipaas.vo.user.PageResult;
import com.ai.paas.ipaas.vo.user.ResponseHeader;
import com.ai.paas.ipaas.vo.user.SelectOrderRequest;
import com.ai.paas.ipaas.vo.user.SelectOrderResponse;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class OrderImpl implements IOrder{
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IOrderSv orderSv;
	
	@Override
	public OrderDetailResponse saveOrderDetail(OrderDetailRequest request)  {	
		OrderDetailResponse response = new OrderDetailResponse();
		ResponseHeader responseHeader = new ResponseHeader();
		try{
			response = orderSv.saveOrderDetail(request);
			responseHeader.setResultCode(Constants.OPERATE_CODE_SUCCESS);
			responseHeader.setResultMessage("操作成功");
		}catch(Exception e){
			logger.error(e.getMessage());
			responseHeader.setResultCode(Constants.OPERATE_CODE_FAIL);
			responseHeader.setResultMessage(e.getMessage());
		}
		response.setResponseHeader(responseHeader);
		return response;
	}

	@Override
	public SelectOrderResponse selectOrderDetails(SelectOrderRequest request) throws PaasException{
		ResponseHeader responseHeader = new ResponseHeader();	
		PageResult<OrderDetailVo>   pageResult  = new PageResult<OrderDetailVo>();
		try{
			pageResult = orderSv.selectOrderDetails(request);
			responseHeader.setResultCode(Constants.OPERATE_CODE_SUCCESS);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			responseHeader.setResultCode(Constants.OPERATE_CODE_FAIL);
			responseHeader.setResultMessage(e.getMessage());
		}		
		SelectOrderResponse response = new SelectOrderResponse();
		response.setPageResult(pageResult);
		response.setResponseHeader(responseHeader);		
		return response;
	}

	@Override
	public SelectOrderResponse selectOrderList(SelectOrderRequest request) throws PaasException{
		ResponseHeader responseHeader = new ResponseHeader();	
		PageResult<OrderDetailVo>   pageResult  = new PageResult<OrderDetailVo>();
		try{
			pageResult = orderSv.selectOrderList(request);
			responseHeader.setResultCode(Constants.OPERATE_CODE_SUCCESS);
		}catch(Exception e){
			responseHeader.setResultCode(Constants.OPERATE_CODE_FAIL);
			e.printStackTrace();
			logger.error(e.getMessage());
		}		
		SelectOrderResponse response = new SelectOrderResponse();
		response.setPageResult(pageResult);
		response.setResponseHeader(responseHeader);		
		return response;
	}

	@Override
	public String applyOrders(String params) {
		OrderDetailRequest request=JSonUtil.fromJSon(params, OrderDetailRequest.class);
		OrderDetailResponse response=saveOrderDetail(request);
		return JSonUtil.toJSon(response);
	}
	
	@Override
	public OrderDetailResponse checkOrders(CheckOrdersRequest request) {
		OrderDetailResponse response = new OrderDetailResponse();
		ResponseHeader responseHeader = new ResponseHeader();
		logger.info("checkOrders start");	
		try{
			response = orderSv.checkOrders(request);
			responseHeader.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
			logger.info("checkOrders success");	
		}catch(PaasException e){
			logger.error(e.getMessage(),e);
			responseHeader.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);		
			responseHeader.setResultMessage(e.getMessage());
		}		
		response.setResponseHeader(responseHeader);		
		return response;
	}
	
}
