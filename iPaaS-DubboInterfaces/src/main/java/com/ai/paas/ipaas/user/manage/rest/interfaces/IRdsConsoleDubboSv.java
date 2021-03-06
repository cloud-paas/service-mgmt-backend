package com.ai.paas.ipaas.user.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.vo.user.ResponseHeader;
import com.ai.paas.ipaas.vo.user.SelectWithNoPageRequest;
import com.ai.paas.ipaas.vo.user.SelectWithNoPageResponse;
import com.ai.paas.ipaas.vo.user.UserProdInstVo;

@Path("/rds/console")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IRdsConsoleDubboSv {
	/**
	 * 查询用户订购的RDS产品列表
	 */
	@Path("/selectUserProdInsts")
	@POST
	public SelectWithNoPageResponse<UserProdInstVo> selectUserProdInsts(SelectWithNoPageRequest<UserProdInstVo> request);
	
	/**
	 * 查询用户订购的RDS的产品列表，根据id
	 */
	@Path("/selectUserProdInstById")
	@POST
	public SelectWithNoPageResponse<UserProdInstVo> selectUserProdInstById(SelectWithNoPageRequest<UserProdInstVo> request);
	
	/**
	 * 停用RDS
	 */
	@Path("/stopRdsContainer")
	@POST
	public ResponseHeader stopRdsContainer(String paraprodBackPara);
	
	/**
	 * 启用RDS
	 */
	@Path("/startRdsContainer")
	@POST
	public ResponseHeader startRdsContainer(String paraprodBackPara);
	
	/**
	 * 销毁RDS
	 */
	@Path("/destroyContainer")
	@POST
	public ResponseHeader destroyContainer(String paraprodBackPara);
	 
}
