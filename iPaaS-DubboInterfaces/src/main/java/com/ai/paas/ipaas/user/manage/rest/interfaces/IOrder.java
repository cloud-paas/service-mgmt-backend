package com.ai.paas.ipaas.user.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.vo.user.CheckOrdersRequest;
import com.ai.paas.ipaas.vo.user.CheckOrdersResponse;
import com.ai.paas.ipaas.vo.user.OrderDetailRequest;
import com.ai.paas.ipaas.vo.user.OrderDetailResponse;
import com.ai.paas.ipaas.vo.user.SelectOrderRequest;
import com.ai.paas.ipaas.vo.user.SelectOrderResponse;

@Path("/user/order")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IOrder {
	@Path("/saveOrderDetail")
	@POST
	public OrderDetailResponse saveOrderDetail(OrderDetailRequest request);
	
	@Path("/selectOrderDetails")
	@POST
	public SelectOrderResponse selectOrderDetails(SelectOrderRequest request) throws PaasException;
	
	@Path("/selectOrderList")
	@POST
	public SelectOrderResponse selectOrderList(SelectOrderRequest request) throws PaasException;

	@Path("/applyOrders")
	@POST
	public String applyOrders(String params);
	
}
