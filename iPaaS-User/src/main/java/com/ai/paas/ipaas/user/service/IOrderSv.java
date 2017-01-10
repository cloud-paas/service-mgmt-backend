package com.ai.paas.ipaas.user.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.dto.OrderDetail;
import com.ai.paas.ipaas.vo.user.CheckOrdersRequest;
import com.ai.paas.ipaas.vo.user.OrderDetailRequest;
import com.ai.paas.ipaas.vo.user.OrderDetailResponse;
import com.ai.paas.ipaas.vo.user.OrderDetailVo;
import com.ai.paas.ipaas.vo.user.PageResult;
import com.ai.paas.ipaas.vo.user.SelectOrderRequest;

public interface IOrderSv {
	public OrderDetailResponse saveOrderDetail(OrderDetailRequest orderDetailVo) throws PaasException;
	
	public PageResult<OrderDetailVo>  selectOrderDetails(SelectOrderRequest request) throws PaasException;
	
	public PageResult<OrderDetailVo>  selectOrderList(SelectOrderRequest request) throws PaasException;

	public OrderDetail selectByPrimaryKey(long orderDetailId) throws Exception;
	
	public OrderDetailResponse checkOrders(CheckOrdersRequest request) throws PaasException;
}
