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

@Path("/idps/console")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IIdpsConsoleDubboSv {
	/**
	 * 查询用户订购的IDPS产品列表
	 */
	@Path("/selectUserProdInsts")
	@POST
	public SelectWithNoPageResponse<UserProdInstVo> selectUserProdInsts(SelectWithNoPageRequest<UserProdInstVo> request);
	
	/**
	 * 停用容器
	 */
	@Path("/stopIdpsContainer")
	@POST
	public ResponseHeader stopIdpsContainer(String paraprodBackPara);
	
	/**
	 * 启用容器
	 */
	@Path("/startIdpsContainer")
	@POST
	public ResponseHeader startIdpsContainer(String paraprodBackPara);
	
	/**
	 * 升级容器
	 */
	@Path("/upgradeContainer")
	@POST
	public ResponseHeader upgradeContainer(String paraprodBackPara);	
	
	/**
	 * 销毁容器
	 */
	@Path("/destroyContainer")
	@POST
	public ResponseHeader destroyContainer(String paraprodBackPara);
	 
}
