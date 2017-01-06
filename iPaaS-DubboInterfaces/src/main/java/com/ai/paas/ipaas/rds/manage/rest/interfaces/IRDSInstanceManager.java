
package com.ai.paas.ipaas.rds.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;

@Path("/rds/mysql/manager")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IRDSInstanceManager extends ISrvManager{

	/**
	 * 服务申请
	 * 相关操作：通过审核的账号可以创建相应的MySQL服务器
	 */
	@Path("/create/master")
	@POST
	public String create(String createApply); 
	
	@Path("/create/slaverorbatmaster")
	@POST
	public String createslobm(String create);

	/**
	 * 注销服务，资源回收
	 * 相关操作：取消账号相关所有服务
	 */
	@Path("/cancel")
	@POST
	public String cancel(String cancelApply);
	
	/**
	 * 获取某种类型的服务所支持的租户管理功能，如申请、注销、启动、停止
	 */
	@Path("/getFuncList")
	@POST
	public String getFuncList();
	
	/**
	 * 启动storm拓扑
	 */
	@Path("/start")
	@POST
	public String start(String startApply);

	/**
	 * 停止storm拓扑
	 */
	@Path("/stop")
	@POST
	public String stop(String stopApply);
	
	/**
	 * 重启服务
	 */
	@Path("/restart")
	@POST
	public String restart(String restartApply);
	
	/**
	 * 获取mysql实例运行状态
	 * 返回全部信息－
	 */
	@Path("/getinstanceinfo")
	@POST
	public String getInstanceInfo(String getinstanceinfo);
	
	/**
	 * 扩容
	 */
	@Path("/modify")
	@POST
	public String modify(String modify);
	
}
