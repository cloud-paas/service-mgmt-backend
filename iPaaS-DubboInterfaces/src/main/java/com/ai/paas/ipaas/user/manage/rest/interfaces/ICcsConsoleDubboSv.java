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

@Path("/iccs/console")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface ICcsConsoleDubboSv {

	/**
	 * 根据用户查询已经开通的服务
	 */
	@Path("/selectServices")
	@POST
	public SelectWithNoPageResponse<UserProdInstVo> selectUserProdInsts(SelectWithNoPageRequest<UserProdInstVo> request);
	
	/**
	 * 根据用户查询已经开通的服务
	 */
	@Path("/cancelServices")
	@POST
	public ResponseHeader cancleUserProdInst(UserProdInstVo vo);

}
