package com.ai.paas.ipaas.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;
import com.ai.paas.ipaas.mds.manage.service.IMsgSrvManager;
import com.ai.paas.ipaas.mds.manage.util.MDSResultWrapper;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;
import com.ai.paas.ipaas.user.constants.Constants;
import com.ai.paas.ipaas.user.dto.MdsGetSubscribeRestfullRes;
import com.ai.paas.ipaas.user.dto.MdsRestfullReq;
import com.ai.paas.ipaas.user.dto.MdsSearchRestfullReq;
import com.ai.paas.ipaas.user.dto.MdsSearchRestfullRes;
import com.ai.paas.ipaas.user.dto.MdsSubscribeRestfullReq;
import com.ai.paas.ipaas.user.dto.MdsSubscribeRestfullRes;
import com.ai.paas.ipaas.user.dto.RestfullReq;
import com.ai.paas.ipaas.user.dto.RestfullReturn;
import com.ai.paas.ipaas.user.dto.UserMgrOperate;
import com.ai.paas.ipaas.user.dto.UserProdInst;
import com.ai.paas.ipaas.user.dto.UserProdInstCriteria;
import com.ai.paas.ipaas.user.service.IMdsConsoleSv;
import com.ai.paas.ipaas.user.service.IUserMgrOperateSv;
import com.ai.paas.ipaas.user.service.dao.UserProdInstMapper;
import com.ai.paas.ipaas.user.utils.DateUtil;
import com.ai.paas.ipaas.util.JSonUtil;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.vo.user.MdsSearchMessageVo;
import com.ai.paas.ipaas.vo.user.MdsUserPageVo;
import com.ai.paas.ipaas.vo.user.MdsUserSubscribeVo;
import com.ai.paas.ipaas.vo.user.ResponseSubPathList;
import com.ai.paas.ipaas.vo.user.UserProdInstVo;
import com.google.gson.Gson;

@Service
@Transactional 
public class MdsConsoleSvImpl implements IMdsConsoleSv{
	
	private final Log logger = LogFactory.getLog(MdsConsoleSvImpl.class);
	@Autowired
	private SqlSessionTemplate template;
	
	@Autowired
	private IUserMgrOperateSv iUserMgrOperateSv;	

	@Autowired
	private IMsgSrvManager msgSrvManager;
	
	@Override
	public List<UserProdInstVo> selectUserProdInsts(UserProdInstVo vo)
			throws PaasException {
		UserProdInstCriteria userProdInstCriteria = new UserProdInstCriteria();
		UserProdInstCriteria.Criteria criteria = userProdInstCriteria.createCriteria();
		criteria.andUserIdEqualTo(vo.getUserId());
		criteria.andUserServiceIdEqualTo(vo.getUserServiceId());
		criteria.andUserServRunStateNotEqualTo(Constants.UserProdInst.UserServRunState.CANCEL);
		
		UserProdInstCriteria.Criteria criteria2 = userProdInstCriteria.createCriteria();		
		criteria2.andUserIdEqualTo(vo.getUserId());
		criteria2.andUserServiceIdEqualTo(vo.getUserServiceId());
		criteria2.andUserServRunStateIsNull();		
		userProdInstCriteria.or(criteria2);
		
		userProdInstCriteria.setOrderByClause(" USER_SERV_OPEN_TIME desc");
		UserProdInstMapper UserProdInstMapper = template.getMapper(UserProdInstMapper.class);
		List<UserProdInst> userProdInsts = UserProdInstMapper.selectByExample(userProdInstCriteria);
		List<UserProdInstVo> userProdInstVoist = new ArrayList<UserProdInstVo>();
		if (userProdInsts != null && userProdInsts.size() > 0) {
			for (int i = 0; i < userProdInsts.size(); i++) {
				UserProdInstVo userProdInstVo = new UserProdInstVo();
				BeanUtils.copyProperties(userProdInsts.get(i), userProdInstVo);
				// 获取容量
				getCapacity(userProdInsts.get(i), userProdInstVo);
				userProdInstVoist.add(userProdInstVo);
			}
		}
		return userProdInstVoist;
	}

	private void getCapacity(UserProdInst userProdInst, UserProdInstVo userProdInstVo) throws PaasException{
		logger.info("查询缓存容量");
		if (userProdInst == null) {
			throw new PaasException("用户产品实例产品为空");
		}
		String prodId = userProdInstVo.getUserServiceId();
		if (StringUtil.isBlank(prodId)) {
			throw new PaasException("用户产品实例产品编码为空");
		}
		String userServParam = userProdInst.getUserServParam();
		UserProdInstVo vo = JSonUtil.fromJSon(userServParam, UserProdInstVo.class);
		userProdInstVo.setCapacity(vo.getCapacity());
	}
	
	@Override
	public List<UserProdInstVo> selectMdsById(UserProdInstVo selectRequestVo)
			throws PaasException {
		UserProdInstCriteria userProdInstCriteria = new UserProdInstCriteria();
		UserProdInstCriteria.Criteria criteria = userProdInstCriteria.createCriteria();	
		long userServId = selectRequestVo.getUserServId();
		criteria.andUserServIdEqualTo(userServId);
		UserProdInstMapper UserProdInstMapper = template.getMapper(UserProdInstMapper.class);
		List<UserProdInst> userProdInsts = UserProdInstMapper.selectByExample(userProdInstCriteria);
		List<UserProdInstVo> userProdInstVoist = new ArrayList<UserProdInstVo>();
		if (userProdInsts != null && userProdInsts.size() > 0) {
			for (int i = 0; i < userProdInsts.size(); i++) {
				UserProdInstVo userProdInstVo = new UserProdInstVo();
				BeanUtils.copyProperties(userProdInsts.get(i), userProdInstVo);
				userProdInstVo.getUserServParam();
				userProdInstVo.setUserServParamMap(selectRequestVo.getUserServParamMap());
				getAmountUsed(userProdInstVo);
				
				userProdInstVoist.add(userProdInstVo);
			}
		}
		return userProdInstVoist;
	}
	
	@Override
	public List<String> getListSubPath(UserProdInstVo selectRequestVo) 
			throws PaasException{
		List<String> userProdInstVoist = null;
		logger.info("查询消息队列下的所有子节点");
		UserProdInstCriteria userProdInstCriteria = new UserProdInstCriteria();
		UserProdInstCriteria.Criteria criteria = userProdInstCriteria.createCriteria();	
		long userServId = selectRequestVo.getUserServId();
		criteria.andUserServIdEqualTo(userServId);
		UserProdInstMapper UserProdInstMapper = template.getMapper(UserProdInstMapper.class);
		List<UserProdInst> userProdInsts = UserProdInstMapper.selectByExample(userProdInstCriteria);
		UserProdInstVo userProdInstVo = new UserProdInstVo();
		if (userProdInsts != null && userProdInsts.size() > 0) {
			for (int i = 0; i < userProdInsts.size(); i++) {
				BeanUtils.copyProperties(userProdInsts.get(i), userProdInstVo);
				userProdInstVo.getUserServParam();
				userProdInstVo.setUserServParamMap(selectRequestVo.getUserServParamMap());
			}
		}
		
		MdsRestfullReq restfullReq = new MdsRestfullReq();
		restfullReq.setUserId(userProdInstVo.getUserId());
		restfullReq.setServiceId(userProdInstVo.getUserServIpaasId());
		restfullReq.setApplyType( "topicUsage");
		Map<String , String> maps = new HashMap<String,String>();
		maps = userProdInstVo.getUserServParamMap();
		String subscribeName = maps.get("subscribeName");
		restfullReq.setSubscribeName(subscribeName);
		Map<String , String> map = new HashMap<String,String>();
		String str = userProdInstVo.getUserServParam();
		map = new Gson().fromJson(str, map.getClass());
		restfullReq.setTopicEnName(map.get("topicEnName"));
		String param = JSonUtil.toJSon(restfullReq);
		
		logger.info("调用查询消息队列下的所有子节点服务接口入参："+param);
		
		String result = "";
		Gson gson = new Gson();
		MsgSrvApply apply = gson.fromJson(param, MsgSrvApply.class);
		try {
			List<String> listSubPaths = msgSrvManager.getListSubPath(apply);
			result = MDSResultWrapper.wraplistSubPathfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
					"Message getListSubPath apply success!", apply, listSubPaths);
		} catch (Exception e) {
			logger.error("Message getListSubPath apply error!", e);
			result = MDSResultWrapper.wraplistSubPathfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message getListSubPath apply failed!" + e.getMessage(), apply,null);
		}
		
		ResponseSubPathList listSubPath = new Gson().fromJson(result, ResponseSubPathList.class);
		if(listSubPath !=null && !"".equals(listSubPath)){
			userProdInstVoist = listSubPath.getListSubPath();
		}
		
		return userProdInstVoist;
	}
	
	public void getAmountUsed(UserProdInstVo userProdInstVo) throws PaasException {
		logger.info("查询已使用量");
		MdsRestfullReq restfullReq = new MdsRestfullReq();
		restfullReq.setUserId(userProdInstVo.getUserId());
		restfullReq.setServiceId(userProdInstVo.getUserServIpaasId());
		restfullReq.setApplyType( "topicUsage");
		
		Map<String , String> maps = new HashMap<String,String>();
		maps = userProdInstVo.getUserServParamMap();
		String subscribeName = maps.get("subscribeName");
		restfullReq.setSubscribeName(subscribeName);
		
		Map<String , String> map = new HashMap<String,String>();
		String str = userProdInstVo.getUserServParam();
		map = new Gson().fromJson(str, map.getClass());
		restfullReq.setTopicEnName(map.get("topicEnName"));
		
		String param = JSonUtil.toJSon(restfullReq);
		logger.info("调用服务接口入参："+param);
		
		String result = "";
		Gson gson = new Gson();
		MsgSrvApply apply = gson.fromJson(param, MsgSrvApply.class);
		try {
			List<MsgSrvUsageApplyResult> usages = msgSrvManager.getTopicUsage(apply);
			result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
					"Message topic usage apply success!", apply, usages);
		} catch (Exception e) {
			logger.error("Message getListSubPath apply error!", e);
			result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic usage apply failed!" + e.getMessage(), apply);
		}
		
		MdsUserPageVo pageVo = new Gson().fromJson(result, MdsUserPageVo.class);
		userProdInstVo.setMdsUserPageVo(pageVo);
	}
	
	
	@Override
	public String cancleUserProdInst(UserProdInstVo vo) throws PaasException {
		long userServId = vo.getUserServId();
		UserProdInstMapper userProdInstMapper = template.getMapper(UserProdInstMapper.class);
		UserProdInst userProdInst = userProdInstMapper.selectByPrimaryKey(userServId);
		if (null == userProdInst) {
			throw new PaasException("用户产品实例不存在");
		}
		
		MdsRestfullReq req = new MdsRestfullReq();
		RestfullReq restfullReq = new RestfullReq();
		req.setUserId(vo.getUserId());
		req.setServiceId(userProdInst.getUserServIpaasId());
		req.setApplyType("cancel");
		String param = JSonUtil.toJSon(restfullReq);
		logger.info("调用服务接口入参："+param);
		
		String result = "";
		Gson gson = new Gson();
		MsgSrvApply apply = gson.fromJson(param, MsgSrvApply.class);
		try {
			msgSrvManager.cancelMessageService(apply);
			result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
					"Message cancel service apply success!", apply);
		} catch (Exception e) {
			logger.error("Message getListSubPath apply error!", e);
			result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message cancel service apply failed!" + e.getMessage(), apply);
		}
		
		RestfullReturn restfullReturn = JSonUtil.fromJSon(result, RestfullReturn.class);
		String resultCode = restfullReturn.getResultCode();
		
		// 修改用户产品实例
		if(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS.equals(resultCode)){
			userProdInst.setUserServRunState(Constants.UserProdInst.UserServRunState.CANCEL);
			userProdInstMapper.updateByPrimaryKey(userProdInst);
		}
		
		// 写用户管理操作
		UserMgrOperate userMgrOperate = new UserMgrOperate();
		userMgrOperate.setUserOperateAction(Constants.UserMgrOperate.UserOperateAction.CANCLE);
		this.makeUserMgrOperate(userProdInst, resultCode, userMgrOperate);
		this.saveUserMgrOperate(userMgrOperate);
		
        return resultCode;
	}
	
	public void makeUserMgrOperate(UserProdInst userProdInst,String resultCode,UserMgrOperate userMgrOperate){
		userMgrOperate.setUserId(userProdInst.getUserId());
		userMgrOperate.setUserProdType(userProdInst.getUserServType());
		userMgrOperate.setUserProdId(userProdInst.getUserServiceId());
		userMgrOperate.setUserServId(userProdInst.getUserServId().toString());
		userMgrOperate.setUserServIpaasId(userProdInst.getUserServIpaasId());
		userMgrOperate.setUserProdParam(userProdInst.getUserServParam());
		userMgrOperate.setUserProdByname(userProdInst.getUserProdByname());
		userMgrOperate.setUserOperateDate(DateUtil.getSysDate());
		if(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS.equals(resultCode)){
			userMgrOperate.setUserOperateResult(Constants.UserMgrOperate.UserOperateResult.SUCCESS);
		}else{
			userMgrOperate.setUserOperateResult(Constants.UserMgrOperate.UserOperateResult.FAIL);
		}
	}
	
	public void saveUserMgrOperate(UserMgrOperate userMgrOperate) throws PaasException{		
		iUserMgrOperateSv.saveUserMgrOperate(userMgrOperate);
	}

	@Override
	public MdsSearchRestfullRes searchMessage(MdsSearchMessageVo vo) throws PaasException{
		long userServId = vo.getUserServId();
		UserProdInstMapper userProdInstMapper = template.getMapper(UserProdInstMapper.class);
		UserProdInst userProdInst = userProdInstMapper.selectByPrimaryKey(userServId);
		if (null == userProdInst) {
			throw new PaasException("用户产品实例不存在");
		}
		String prodId = userProdInst.getUserServiceId();
		if (StringUtil.isBlank(prodId)) {
			throw new PaasException("用户产品实例产品编码为空");
		}
		
		MdsSearchRestfullReq restfullReq = new MdsSearchRestfullReq();
		restfullReq.setUserId(vo.getUserId());
		restfullReq.setServiceId(userProdInst.getUserServIpaasId());
		restfullReq.setOffset(vo.getOffset());
		restfullReq.setPartition(vo.getPartition());
		restfullReq.setTopicEnName(vo.getTopicEnName());
		restfullReq.setApplyType("getMessage");
		String param = JSonUtil.toJSon(restfullReq);
		logger.info("调用服务接口入参："+param);
		
		String result = "";
		Gson gson = new Gson();
		MsgSrvApply apply = gson.fromJson(param, MsgSrvApply.class);
		try {
			String message = msgSrvManager.getTopicMessage(apply);
			if (null == message) {
				result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
						"Get topic message apply failed!", apply);
			} else {
				result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
						"Get topic message apply success!", apply, message);
			}
		} catch (Exception e) {
			logger.error("Message getListSubPath apply error!", e);
			result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply failed!" + e.getMessage(), apply);
		}

        return JSonUtil.fromJSon(result, MdsSearchRestfullRes.class);
	}
	
	@Override
	public MdsSubscribeRestfullRes createSubscribe(MdsUserSubscribeVo vo)  throws PaasException{
		//组织逻辑参数(UserServId 要对应上)
		MdsSubscribeRestfullReq restfullReq = new MdsSubscribeRestfullReq();
		restfullReq.setUserId(vo.getUserId());
		restfullReq.setTopicEnName(vo.getTopicEnName());
		restfullReq.setSubscribeName(vo.getSubscribeName());
		restfullReq.setUserServIpaasId(vo.getUserServIpaasId());
		String param = JSonUtil.toJSon(restfullReq);
		logger.info("调用mds订阅服务接口入参："+param);
		
		String result = "";
		Gson gson = new Gson();
		MdsUserSubscribe apply = gson.fromJson(param, MdsUserSubscribe.class);
		try {
			msgSrvManager.createSubscribe(apply);
			result = MDSResultWrapper.wrapSubRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
					"Message create subscribe apply success!", null);
		} catch (Exception e) {
			logger.error("Message getListSubPath apply error!", e);
			result = MDSResultWrapper.wrapSubRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message create subscribe apply failed!" + e.getMessage(), apply);
		}
		
        return JSonUtil.fromJSon(result, MdsSubscribeRestfullRes.class);
	}
	
	@Override
	public MdsGetSubscribeRestfullRes getSubscribe(MdsUserSubscribeVo vo)  throws PaasException{
		MdsSubscribeRestfullReq restfullReq = new MdsSubscribeRestfullReq();
		restfullReq.setTopicEnName(vo.getTopicEnName());
		restfullReq.setSubscribeName(vo.getSubscribeName());
		String param = JSonUtil.toJSon(restfullReq);
		logger.info("查询之前是否订阅，调用mds订阅服务接口入参："+param);
		
		String result = "";
		Gson gson = new Gson();
		MdsUserSubscribe apply = gson.fromJson(param, MdsUserSubscribe.class);
		try {
			String isExis = "no";
			List<MdsUserSubscribe> subs = msgSrvManager.getSubscribe(apply);
			if(!subs.isEmpty()){
				isExis = "yes";
			}
			result = MDSResultWrapper.wrapSubRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
					"Message subscribe usage apply success!", null, isExis);
		} catch (Exception e) {
			logger.error("Message getListSubPath apply error!", e);
			result = MDSResultWrapper.wrapSubRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message subscribe usage apply failed!"+ e.getMessage(), apply);
		}
		
        return JSonUtil.fromJSon(result, MdsGetSubscribeRestfullRes.class);
	}
	
	@Override
	public String resendMessage(String params) throws PaasException {
		JSONObject object = new JSONObject(params);
		object.put("applyType", "send");
		logger.info("调用服务接口入参："+object.toString());
		
		String result = "";
		Gson gson = new Gson();
		MsgSrvApply apply = gson.fromJson(object.toString(), MsgSrvApply.class);
		try {
			msgSrvManager.sendMessage(apply);
			result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
					"Send topic message apply success!", apply, "");
			
		} catch (Exception e) {
			logger.error("MessageServiceManager send topic message error!", e);
			result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Send topic message apply failed!" + e.getMessage(), apply);
		}
		
		JSONObject resultinfo = new JSONObject(result);
		if(resultinfo.get("resultCode").equals(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL)){
			throw new PaasException(resultinfo.getString("resultMsg"));
		}
		
		return result;
	}

	@Override
	public String skipMessage(String params) throws PaasException {
		JSONObject object=new JSONObject(params);
		object.put("applyType", "adjust");
		String data=object.toString();
		logger.info("调用服务接口入参："+data);
		
		String result = "";
		Gson gson = new Gson();
		MsgSrvApply apply = gson.fromJson(object.toString(), MsgSrvApply.class);
		try {
			msgSrvManager.adjustTopicOffset(apply);
			result = MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
					"Get topic message apply success!", apply);
			
		} catch (Exception e) {
			logger.error("Adjust topic offset apply error!", e);
			MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Adjust topic offset apply failed!", null);
		}
		
		JSONObject resultinfo=new JSONObject(result);
		if(resultinfo.get("resultCode").equals(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL)){
			throw new PaasException(resultinfo.getString("resultMsg"));
		}
		
		return result;
	}
	
}
