package com.ai.paas.ipaas.user.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;
import com.ai.paas.ipaas.ses.service.ISesManage;
import com.ai.paas.ipaas.ses.service.constant.SesConstants;
import com.ai.paas.ipaas.ses.service.vo.SesSrvApplyResult;
import com.ai.paas.ipaas.user.constants.Constants;
import com.ai.paas.ipaas.user.dto.RestfullReq;
import com.ai.paas.ipaas.user.dto.RestfullReturn;
import com.ai.paas.ipaas.user.dto.UserMgrOperate;
import com.ai.paas.ipaas.user.dto.UserProdInst;
import com.ai.paas.ipaas.user.service.ISesConsoleSv;
import com.ai.paas.ipaas.user.service.IUserMgrOperateSv;
import com.ai.paas.ipaas.user.service.dao.UserProdInstMapper;
import com.ai.paas.ipaas.user.utils.DateUtil;
import com.ai.paas.ipaas.util.JSonUtil;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.vo.user.UserProdInstVo;
import com.google.gson.Gson;

@Service
@Transactional
public class SesConsoleSvImpl implements ISesConsoleSv {
	@Autowired
	private SqlSessionTemplate template;

	@Autowired
	private IUserMgrOperateSv userMgrOperateSv;

	@Autowired
	ISesManage sesSrv;

	@Override
	public String startService(UserProdInstVo vo) throws PaasException, IOException, URISyntaxException {
		long userServId = vo.getUserServId();
		UserProdInstMapper userProdInstMapper = template.getMapper(UserProdInstMapper.class);
		UserProdInst userProdInst = userProdInstMapper.selectByPrimaryKey(userServId);
		if (userProdInst == null) {
			throw new PaasException("产品实例为空！");
		}

		RestfullReq req = new RestfullReq();
		req.setUserId(vo.getUserId());
		req.setApplyType(SesConstants.APPLY_TYPE_START);
		req.setServiceId(userProdInst.getUserServIpaasId());
		String param = JSonUtil.toJSon(req);

		Gson gson = new Gson();
		ApplyInfo operateApplyParam = gson.fromJson(param, ApplyInfo.class);
		SesSrvApplyResult result = new SesSrvApplyResult();
		result.setUserId(operateApplyParam.getUserId());
		result.setServiceId(operateApplyParam.getServiceId());
		result.setApplyType(operateApplyParam.getApplyType());
		try {
			sesSrv.start(operateApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg(SesConstants.SES_MANAGE_SUCCESS);
		} catch (PaasException e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg(SesConstants.SES_MANAGE_FAIL);
			return new Gson().toJson(result);
		}

		String data = new Gson().toJson(result);

		RestfullReturn restfullReturn = JSonUtil.fromJSon(data, RestfullReturn.class);
		String resultCode = restfullReturn.getResultCode();
		if (PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS.equals(resultCode)) {
			userProdInst.setUserServRunState(Constants.UserProdInst.UserServRunState.OPEN);
			userProdInstMapper.updateByPrimaryKey(userProdInst);
		}
		UserMgrOperate userMgrOperate = new UserMgrOperate();
		userMgrOperate.setUserOperateAction(Constants.UserMgrOperate.UserOperateAction.START);
		this.saveUserMgrOperate(userProdInst, resultCode, userMgrOperate);

		return resultCode;
	}

	@Override
	public String stopService(UserProdInstVo vo) throws PaasException, IOException, URISyntaxException {
		long userServId = vo.getUserServId();
		UserProdInstMapper userProdInstMapper = template.getMapper(UserProdInstMapper.class);
		UserProdInst userProdInst = userProdInstMapper.selectByPrimaryKey(userServId);
		if (userProdInst == null) {
			throw new PaasException("产品实例为空！");
		}

		RestfullReq req = new RestfullReq();
		req.setUserId(vo.getUserId());
		req.setApplyType(SesConstants.APPLY_TYPE_STOP);
		req.setServiceId(userProdInst.getUserServIpaasId());
		String param = JSonUtil.toJSon(req);

		Gson gson = new Gson();
		ApplyInfo operateApplyParam = gson.fromJson(param, ApplyInfo.class);
		SesSrvApplyResult result = new SesSrvApplyResult();
		result.setUserId(operateApplyParam.getUserId());
		result.setServiceId(operateApplyParam.getServiceId());
		result.setApplyType(operateApplyParam.getApplyType());
		try {
			sesSrv.stop(operateApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg(SesConstants.SES_MANAGE_SUCCESS);
		} catch (PaasException e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg(SesConstants.SES_MANAGE_FAIL);
			return new Gson().toJson(result);
		}

		String data = new Gson().toJson(result);

		RestfullReturn restfullReturn = JSonUtil.fromJSon(data, RestfullReturn.class);
		String resultCode = restfullReturn.getResultCode();
		if (PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS.equals(resultCode)) {
			userProdInst.setUserServRunState(Constants.UserProdInst.UserServRunState.CLOSE);
			userProdInstMapper.updateByPrimaryKey(userProdInst);

		}
		UserMgrOperate userMgrOperate = new UserMgrOperate();
		userMgrOperate.setUserOperateAction(Constants.UserMgrOperate.UserOperateAction.STOP);
		this.saveUserMgrOperate(userProdInst, resultCode, userMgrOperate);
		return resultCode;
	}

	@Override
	public String cancleService(UserProdInstVo vo) throws PaasException, IOException, URISyntaxException {
		long userServId = vo.getUserServId();
		UserProdInstMapper userProdInstMapper = template.getMapper(UserProdInstMapper.class);
		UserProdInst userProdInst = userProdInstMapper.selectByPrimaryKey(userServId);
		if (userProdInst == null) {
			throw new PaasException("产品实例为空！");
		}

		RestfullReq req = new RestfullReq();
		req.setUserId(vo.getUserId());
		req.setApplyType(SesConstants.APPLY_TYPE_RECYCLE);
		req.setServiceId(userProdInst.getUserServIpaasId());
		String param = JSonUtil.toJSon(req);

		Gson gson = new Gson();
		ApplyInfo operateApplyParam = gson.fromJson(param, ApplyInfo.class);
		SesSrvApplyResult result = new SesSrvApplyResult();
		result.setUserId(operateApplyParam.getUserId());
		result.setServiceId(operateApplyParam.getServiceId());
		result.setApplyType(operateApplyParam.getApplyType());
		try {
			sesSrv.recycle(operateApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg(SesConstants.SES_MANAGE_SUCCESS);
		} catch (PaasException e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg(SesConstants.SES_MANAGE_FAIL);
			return new Gson().toJson(result);
		}

		String data = new Gson().toJson(result);

		RestfullReturn restfullReturn = JSonUtil.fromJSon(data, RestfullReturn.class);
		String resultCode = restfullReturn.getResultCode();
		if (PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS.equals(resultCode)) {
			userProdInst.setUserServRunState(Constants.UserProdInst.UserServRunState.CANCEL);
			userProdInstMapper.updateByPrimaryKey(userProdInst);
		}
		UserMgrOperate userMgrOperate = new UserMgrOperate();
		userMgrOperate.setUserOperateAction(Constants.UserMgrOperate.UserOperateAction.CANCLE);
		this.saveUserMgrOperate(userProdInst, resultCode, userMgrOperate);

		return resultCode;
	}

	private void saveUserMgrOperate(UserProdInst userProdInst, String resultCode, UserMgrOperate userMgrOperate)
			throws PaasException {
		userMgrOperate.setUserId(userProdInst.getUserId());
		userMgrOperate.setUserProdType(userProdInst.getUserServType());
		userMgrOperate.setUserProdId(userProdInst.getUserServiceId());
		userMgrOperate.setUserServIpaasId(userProdInst.getUserServIpaasId());
		userMgrOperate.setUserServId(StringUtil.toString(userProdInst.getUserServId()));
		userMgrOperate.setUserProdParam(userProdInst.getUserServParam());
		userMgrOperate.setUserProdByname(userProdInst.getUserProdByname());
		userMgrOperate.setUserOperateDate(DateUtil.getSysDate());
		if (PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS.equals(resultCode)) {
			userMgrOperate.setUserOperateResult(Constants.UserMgrOperate.UserOperateResult.SUCCESS);
		} else {
			userMgrOperate.setUserOperateResult(Constants.UserMgrOperate.UserOperateResult.FAIL);
		}
		userMgrOperateSv.saveUserMgrOperate(userMgrOperate);
	}

}
