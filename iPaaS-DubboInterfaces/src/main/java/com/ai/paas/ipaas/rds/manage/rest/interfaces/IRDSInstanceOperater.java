package com.ai.paas.ipaas.rds.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rds/mysql/operater")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IRDSInstanceOperater {

	/**
	 * 切换主备服务器
	 * @param switchmaster
	 * @return
	 */
	@Path("/switchmaster")
	@POST
	public String switchmaster(String switchmaster);
	
	@Path("/changecontainerconfig")
	@POST
	public String changecontainerconfig(String changecontainerconfig);
	
}
