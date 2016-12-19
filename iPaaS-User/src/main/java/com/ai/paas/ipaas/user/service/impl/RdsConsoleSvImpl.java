package com.ai.paas.ipaas.user.service.impl;

import java.util.ArrayList;
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

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rds.service.IRdsSv;
import com.ai.paas.ipaas.user.constants.Constants;
import com.ai.paas.ipaas.user.dto.ProdProduct;
import com.ai.paas.ipaas.user.dto.UserProdInst;
import com.ai.paas.ipaas.user.dto.UserProdInstCriteria;
import com.ai.paas.ipaas.user.service.IProdProductSv;
import com.ai.paas.ipaas.user.service.IRdsConsoleSv;
import com.ai.paas.ipaas.user.service.IUserProdInstSv;
import com.ai.paas.ipaas.user.service.dao.UserProdInstMapper;
import com.ai.paas.ipaas.user.utils.JsonUtils;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.vo.user.ResponseHeader;
import com.ai.paas.ipaas.vo.user.UserProdInstVo;
import com.google.gson.Gson;

@Service
@Transactional
public class RdsConsoleSvImpl implements IRdsConsoleSv {
	private final Log logger = LogFactory.getLog(RdsConsoleSvImpl.class);

	@Autowired
	private SqlSessionTemplate template;

	@Autowired
	private IProdProductSv iProdProductSv;

	@Autowired
	private IUserProdInstSv iUserProdInstSv;

	@Autowired
	private IRdsSv iRdsSv;

	@Override
	public List<UserProdInstVo> selectUserProdInsts(UserProdInstVo vo) throws PaasException {
		UserProdInstCriteria userProdInstCriteria = new UserProdInstCriteria();
		UserProdInstCriteria.Criteria criteria = userProdInstCriteria.createCriteria();
		criteria.andUserIdEqualTo(vo.getUserId()).andUserProdBynameEqualTo("RDS");
		criteria.andUserServRunStateNotEqualTo(Constants.UserProdInst.UserServRunState.CANCEL);

		UserProdInstCriteria.Criteria criteria2 = userProdInstCriteria.createCriteria();
		criteria2.andUserIdEqualTo(vo.getUserId());
		criteria2.andUserProdBynameEqualTo("RDS");
		criteria2.andUserServRunStateIsNull();
		userProdInstCriteria.or(criteria2);

		UserProdInstMapper UserProdInstMapper = template.getMapper(UserProdInstMapper.class);
		List<UserProdInst> userProdInsts = UserProdInstMapper.selectByExample(userProdInstCriteria);
		List<UserProdInstVo> userProdInstVoist = new ArrayList<UserProdInstVo>();
		if (userProdInsts != null && userProdInsts.size() > 0) {
			for (int i = 0; i < userProdInsts.size(); i++) {
				UserProdInstVo userProdInstVo = new UserProdInstVo();
				BeanUtils.copyProperties(userProdInsts.get(i), userProdInstVo);

				// 获得ProName-------start
				String prodId = userProdInstVo.getUserServiceId();
				if (StringUtil.isBlank(prodId)) {
					throw new PaasException("用户产品实例产品编码为空");
				}
				short priKey = Short.parseShort(prodId);
				ProdProduct prodProduct = iProdProductSv.selectProductByPrimaryKey(priKey);
				userProdInstVo.setProdName(prodProduct.getProdName());
				// 获得ProName-------end

				// 获得UserServParam字段中对应map的值---------start
				String prodParam = userProdInstVo.getUserServParam();
				String prodBackParam = userProdInstVo.getUserServBackParam();
				Gson gson = new Gson();
				Map<String, String> map = gson.fromJson(prodParam, Map.class);
				Map<String, Object> mapBack = gson.fromJson(prodBackParam, Map.class);
				String serviceName = map.get("serviceName");
				if (serviceName == null) {
					serviceName = "";
				}
				userProdInstVo.setServiceName(serviceName);
				// 获得UserServParam字段中对应map的值---------end
				userProdInstVo.setUserServParamMap(map);
				userProdInstVo.setUserServBackParamMap(mapBack);
				userProdInstVo.setUserServBackParam(prodBackParam.replaceAll("\"", ""));
				userProdInstVoist.add(userProdInstVo);
			}
		}
		return userProdInstVoist;
	}

	public List<UserProdInstVo> selectUserProdInstById(UserProdInstVo vo) throws PaasException {
		UserProdInstCriteria userProdInstCriteria = new UserProdInstCriteria();
		UserProdInstCriteria.Criteria criteria = userProdInstCriteria.createCriteria();
		criteria.andUserIdEqualTo(vo.getUserId());
		criteria.andUserServIdEqualTo(vo.getUserServId());
		criteria.andUserIdEqualTo(vo.getUserId()).andUserProdBynameEqualTo("RDS");

		UserProdInstMapper UserProdInstMapper = template.getMapper(UserProdInstMapper.class);
		List<UserProdInst> userProdInsts = UserProdInstMapper.selectByExample(userProdInstCriteria);
		List<UserProdInstVo> userProdInstVoist = new ArrayList<UserProdInstVo>();
		if (userProdInsts != null && userProdInsts.size() > 0) {
			for (int i = 0; i < userProdInsts.size(); i++) {
				UserProdInstVo userProdInstVo = new UserProdInstVo();
				BeanUtils.copyProperties(userProdInsts.get(i), userProdInstVo);

				// 获得ProName-------start
				String prodId = userProdInstVo.getUserServiceId();
				if (StringUtil.isBlank(prodId)) {
					throw new PaasException("用户产品实例产品编码为空");
				}
				short priKey = Short.parseShort(prodId);
				ProdProduct prodProduct = iProdProductSv.selectProductByPrimaryKey(priKey);
				userProdInstVo.setProdName(prodProduct.getProdName());
				// 获得ProName-------end

				// 获得UserServParam字段中对应map的值---------start
				String prodParam = userProdInstVo.getUserServParam();
				String prodBackParam = userProdInstVo.getUserServBackParam();
				Gson gson = new Gson();
				Map<String, String> map = gson.fromJson(prodParam, Map.class);
				Map<String, Object> mapBack = gson.fromJson(prodBackParam, Map.class);
				String serviceName = map.get("serviceName");
				if (serviceName == null) {
					serviceName = "";
				}
				userProdInstVo.setServiceName(serviceName);
				// 获得UserServParam字段中对应map的值---------end
				userProdInstVo.setUserServParamMap(map);
				userProdInstVo.setUserServBackParamMap(mapBack);
				userProdInstVo.setUserServBackParam(prodBackParam.replaceAll("\"", ""));
				userProdInstVoist.add(userProdInstVo);
			}
		}

		return userProdInstVoist;
	}

	public ResponseHeader stopRdsContainer(String paraprodBackPara) throws PaasException {
		ResponseHeader responseHeader = new ResponseHeader();
		
		try {
			logger.info("调用停止rds容器服务接口入参：" + "{\"instanceid\":" + paraprodBackPara + "}");
			String result = iRdsSv.stop(paraprodBackPara);
			logger.info("调用停止rds容器服务接口结果：" + result);

			JSONObject json = new JSONObject();
			json = JsonUtils.parse(result);
			responseHeader.setResultCode(json.getString("resultCode"));
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			logger.error(errorMessage, e);
			throw new PaasException("停止rds容器服务异常");
		}
		
		return responseHeader;
	}

	public ResponseHeader startRdsContainer(String paraprodBackPara) throws PaasException {
		ResponseHeader responseHeader = new ResponseHeader();
		logger.info("调用启用rds容器服务接口");
		try {
			logger.info("调用启动rds容器服务接口入参：" + "{\"instanceid\":" + paraprodBackPara + "}");
			String result = iRdsSv.start(paraprodBackPara);
			logger.info("调用启动rds容器服务接口结果：" + result);

			JSONObject json = new JSONObject();
			json = JsonUtils.parse(result);
			responseHeader.setResultCode(json.getString("resultCode"));
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			logger.error(errorMessage, e);
			throw new PaasException("启动rds容器服务异常");
		}
		
		return responseHeader;
	}

	public ResponseHeader cleanRdsContainer(String paraprodBackPara, String destroy) throws PaasException {
		ResponseHeader responseHeader = new ResponseHeader();
		return responseHeader;
	}

	@Override
	public ResponseHeader destroyContainer(String paraprodBackPara) throws PaasException {
		ResponseHeader responseHeader = new ResponseHeader();
		try {
			logger.info("调用注销rds容器服务接口入参：" + "{\"instanceid\":" + paraprodBackPara + "}");
			String result = iRdsSv.cancel(paraprodBackPara);
			logger.info("调用注销rds容器服务接口结果：" + result);

			JSONObject json = new JSONObject();
			json = JsonUtils.parse(result);
			responseHeader.setResultCode(json.getString("resultCode"));
			
			if(json.getString("resultMsg").equals("SUCCESS")) {
				// 更新记录表
				Gson gson = new Gson();
				Map<String, Object> mapBack = gson.fromJson(paraprodBackPara, Map.class);
				String userServId = mapBack.get("userServId").toString();
				updateUserprodInstById(userServId);
			}
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			logger.error(errorMessage, e);
			throw new PaasException("注销rds容器服务异常");
		}
		
		return responseHeader;
	}

	// 更新订单实例的状态
	public void updateUserprodInstById(String userServId) throws PaasException {
		UserProdInst uerProdInst = iUserProdInstSv.selectUserProdInstsById(Long.valueOf(userServId));
		uerProdInst.setUserServRunState("2");
		iUserProdInstSv.updateUserprodInstById(uerProdInst);
	}

}
