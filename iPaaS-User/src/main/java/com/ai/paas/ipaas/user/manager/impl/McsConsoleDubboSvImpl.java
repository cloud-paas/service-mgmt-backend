package com.ai.paas.ipaas.user.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.manage.rest.interfaces.IMcsConsoleDubboSv;
import com.ai.paas.ipaas.user.service.IMcsConsoleSv;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.vo.user.ResponseHeader;
import com.ai.paas.ipaas.vo.user.SelectWithNoPageRequest;
import com.ai.paas.ipaas.vo.user.SelectWithNoPageResponse;
import com.ai.paas.ipaas.vo.user.UserProdInstVo;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class McsConsoleDubboSvImpl implements IMcsConsoleDubboSv {

    private final Log logger = LogFactory.getLog(McsConsoleDubboSvImpl.class);

    @Autowired
    private IMcsConsoleSv iMcsConsoleSv;

    private void validate(UserProdInstVo vo) throws PaasException {
        if (null == vo) {
            throw new PaasException("入参对象为空！");
        }
        if (StringUtil.isBlank(vo.getUserId())) {
            throw new PaasException("用户编码为空！");
        }
    }

    @Override
    public SelectWithNoPageResponse<UserProdInstVo> selectUserProdInsts(
            SelectWithNoPageRequest<UserProdInstVo> request) {
        ResponseHeader responseHeader = new ResponseHeader();
        List<UserProdInstVo> resultList = new ArrayList<UserProdInstVo>();
        try {
            this.validate(request.getSelectRequestVo());
            resultList = iMcsConsoleSv.selectUserProdInsts(request.getSelectRequestVo());
            responseHeader.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseHeader.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            responseHeader.setResultMessage(e.getMessage());
        }
        SelectWithNoPageResponse<UserProdInstVo> response = new SelectWithNoPageResponse<UserProdInstVo>();
        response.setResultList(resultList);
        response.setResponseHeader(responseHeader);
        return response;
    }

    /**
     * @description MCS控制台操作
     * @param vo
     * @throws Exception
     * @author caiyt
     */
    public ResponseHeader operatMcsServer(UserProdInstVo vo) throws Exception {
    	return iMcsConsoleSv.operatMcsServer(vo);
    }

    public SelectWithNoPageResponse<UserProdInstVo> selectMcsById(
            SelectWithNoPageRequest<UserProdInstVo> request) throws PaasException {
        ResponseHeader responseHeader = new ResponseHeader();
        List<UserProdInstVo> resultList = new ArrayList<UserProdInstVo>();
        try {
            this.validate(request.getSelectRequestVo());
            resultList = iMcsConsoleSv.selectMcsById(request.getSelectRequestVo());
            responseHeader.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseHeader.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            responseHeader.setResultMessage(e.getMessage());
        }
        SelectWithNoPageResponse<UserProdInstVo> response = new SelectWithNoPageResponse<UserProdInstVo>();
        response.setResultList(resultList);
        response.setResponseHeader(responseHeader);
        return response;
    }

    public ResponseHeader mdyServPwd(UserProdInstVo vo) {
        ResponseHeader responseHeader = new ResponseHeader();
        try {
            this.validate(vo);
            if (StringUtil.isBlank(vo.getNewPwd())) {
                throw new PaasException("Pass服务新密码为空");
            }
            if (StringUtil.isBlank(vo.getOldPwd())) {
                throw new PaasException("Pass服务旧密码为空");
            }
            responseHeader = iMcsConsoleSv.mdyServPwd(vo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseHeader.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            responseHeader.setResultMessage(e.getMessage());
        }
        return responseHeader;
    }
}