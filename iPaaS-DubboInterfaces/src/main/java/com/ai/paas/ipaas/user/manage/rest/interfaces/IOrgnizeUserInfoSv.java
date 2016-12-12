package com.ai.paas.ipaas.user.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.vo.user.OrgnizeUserInfoVo;

@Path("/orgnize/userInfo")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public interface IOrgnizeUserInfoSv {

	@Path("/getOrgnizeUserInfo")
	@POST
	public OrgnizeUserInfoVo getOrgnizeUserInfo(String userId) throws PaasException;
	
	@Path("/saveOrgnizeUserInfo")
	@POST
	public void saveOrgnizeUserInfo(OrgnizeUserInfoVo orgnizeUser) throws PaasException;
	
	@Path("/getOrgUsrInfoCntByOrgId")
	@POST
	public int getOrgUsrInfoCntByOrgId(Integer orgId) throws PaasException;
	
	/*@Path("/getOrgCodeByUserId")
	@POST
	@Select("SELECT b.org_code AS orgCode " +
			"  FROM orgnize_user_info a left join orgnize_center b " +
			"       on a.org_id = b.org_id " +
			" WHERE a.user_id = #{userId} ")
	public String getOrgCodeByUserId(String userId);
	*/
}
