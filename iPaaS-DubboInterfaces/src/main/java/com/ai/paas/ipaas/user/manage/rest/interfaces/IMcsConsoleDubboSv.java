package com.ai.paas.ipaas.user.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.vo.user.ResponseHeader;
import com.ai.paas.ipaas.vo.user.SelectWithNoPageRequest;
import com.ai.paas.ipaas.vo.user.SelectWithNoPageResponse;
import com.ai.paas.ipaas.vo.user.UserProdInstVo;

@Path("/mcs/console")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IMcsConsoleDubboSv {

    /**
     * 缓存控制台数据查询
     * 
     * @param request
     * @return
     */
    @Path("/selectUserProdInsts")
    @POST
    public SelectWithNoPageResponse<UserProdInstVo> selectUserProdInsts(
            SelectWithNoPageRequest<UserProdInstVo> request);

    /**
     * @description 清空MCS实例所有数据
     * @param vo
     * @return
     * @throws Exception
     * @author caiyt
     */
    @Path("/cleanMCS")
    @POST
    public ResponseHeader operatMcsServer(UserProdInstVo vo) throws Exception;

    @Path("/selectMcsById")
    @POST
    public SelectWithNoPageResponse<UserProdInstVo> selectMcsById(
            SelectWithNoPageRequest<UserProdInstVo> request) throws PaasException;

    @Path("/mdyServPwd")
    @POST
    public ResponseHeader mdyServPwd(UserProdInstVo vo);
}