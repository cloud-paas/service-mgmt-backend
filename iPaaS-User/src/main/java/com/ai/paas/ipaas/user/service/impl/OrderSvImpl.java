package com.ai.paas.ipaas.user.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.user.constants.Constants;
import com.ai.paas.ipaas.user.dto.OrderDetail;
import com.ai.paas.ipaas.user.dto.OrderDetailCriteria;
import com.ai.paas.ipaas.user.dto.ProdProduct;
import com.ai.paas.ipaas.user.dto.UserCenter;
import com.ai.paas.ipaas.user.dto.UserMessage;
import com.ai.paas.ipaas.user.dto.UserProdInst;
import com.ai.paas.ipaas.user.dto.UserProdInstCriteria;
import com.ai.paas.ipaas.user.service.IOrderSv;
import com.ai.paas.ipaas.user.service.IProdProductSv;
import com.ai.paas.ipaas.user.service.ISysParamSv;
import com.ai.paas.ipaas.user.service.dao.OrderDetailMapper;
import com.ai.paas.ipaas.user.service.dao.ProdProductMapper;
import com.ai.paas.ipaas.user.service.dao.UserCenterMapper;
import com.ai.paas.ipaas.user.service.dao.UserMessageMapper;
import com.ai.paas.ipaas.user.service.dao.UserProdInstMapper;
import com.ai.paas.ipaas.user.utils.DateUtil;
import com.ai.paas.ipaas.user.utils.EmailTemplUtil;
import com.ai.paas.ipaas.user.utils.JsonUtils;
import com.ai.paas.ipaas.user.utils.PageUtils;
import com.ai.paas.ipaas.user.utils.gson.GsonUtil;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.util.UUIDTool;
import com.ai.paas.ipaas.vo.user.CheckOrdersRequest;
import com.ai.paas.ipaas.vo.user.EmailDetail;
import com.ai.paas.ipaas.vo.user.OrderDetailRequest;
import com.ai.paas.ipaas.vo.user.OrderDetailResponse;
import com.ai.paas.ipaas.vo.user.OrderDetailVo;
import com.ai.paas.ipaas.vo.user.PageEntity;
import com.ai.paas.ipaas.vo.user.PageResult;
import com.ai.paas.ipaas.vo.user.SelectOrderRequest;
import com.ai.paas.ipaas.vo.user.SysParamVo;
import com.ai.paas.ipaas.vo.user.SysParmRequest;
import com.ai.paas.ipaas.zookeeper.SystemConfigHandler;

@Service
@Transactional
public class OrderSvImpl implements IOrderSv {
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SqlSessionTemplate template;

	@Autowired
	private IProdProductSv iProdProductSv;

	@Autowired
	private ISysParamSv iSysParamSv;

	@Override
	public OrderDetailResponse saveOrderDetail(OrderDetailRequest request) throws PaasException {
		OrderDetailMapper orderDetailMapper = template.getMapper(OrderDetailMapper.class);
		ProdProduct prodProduct = iProdProductSv.selectProductByPrimaryKey(Short.valueOf(request.getProdId()));
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOperateType(request.getOperateType());
		orderDetail.setUserId(request.getUserId());
		orderDetail.setOrgCode(request.getOrgCode());
		orderDetail.setProdType(prodProduct.getProdType());
		orderDetail.setProdId(prodProduct.getProdId().toString());
		orderDetail.setProdByname(prodProduct.getProdEnSimp());
		orderDetail.setProdParam(request.getProdParam());
		orderDetail.setUserServIpaasPwd(request.getUserServIpaasPwd());
		orderDetail.setOrderAppDate(DateUtil.getSysDate());
		orderDetail.setOrderCheckStatus(Constants.Order.OrderCheckStatus.CHECK_STATUS_AUDITING);
		orderDetail.setOrderStatus(Constants.Order.PaasOrderStatus.ORDER_STATUS_EXECUTE);
		orderDetailMapper.insert(orderDetail);

		List<EmailDetail> emailList = new ArrayList<EmailDetail>();
		EmailDetail email = getAuditPointEmail(orderDetail);
		emailList.add(email);
		OrderDetailResponse rsp = new OrderDetailResponse();
		rsp.setEmail(emailList);
		rsp.setNeedSend(true);

		return rsp;
	}

	@Override
	public PageResult<OrderDetailVo> selectOrderDetails(SelectOrderRequest request) throws PaasException {
		PageEntity pageEntity = request.getPageEntity();

		OrderDetailCriteria orderDetailCriteria = new OrderDetailCriteria();
		OrderDetailCriteria.Criteria criteria = orderDetailCriteria.createCriteria();
		criteria.andOrderCheckStatusEqualTo(Constants.Order.OrderCheckStatus.CHECK_STATUS_AUDITING);
		criteria.andOrderStatusEqualTo(Constants.Order.PaasOrderStatus.ORDER_STATUS_EXECUTE);

		orderDetailCriteria.setOrderByClause(" order_app_date desc");
		OrderDetailMapper orderDetailMapper = template.getMapper(OrderDetailMapper.class);
		int totalCount = orderDetailMapper.countByExample(orderDetailCriteria);

		orderDetailCriteria.setLimitStart(pageEntity.getLimitStart());
		orderDetailCriteria.setLimitEnd(pageEntity.getLimitEnd());

		List<OrderDetail> orderDetails = orderDetailMapper.selectByExampleWithBLOBs(orderDetailCriteria);

		List<OrderDetailVo> orderDetailVoList = new ArrayList<OrderDetailVo>();
		if (orderDetails != null && orderDetails.size() > 0) {
			for (int i = 0; i < orderDetails.size(); i++) {
				OrderDetailVo orderDetailVo = new OrderDetailVo();
				BeanUtils.copyProperties(orderDetails.get(i), orderDetailVo);
				orderDetailVoList.add(orderDetailVo);
			}
			// 查询user信息
			getUserInfo(orderDetailVoList);
		}

		PageResult<OrderDetailVo> pageResult = new PageResult<OrderDetailVo>();
		pageResult.setResultList(orderDetailVoList);
		pageResult.setTotalPages(PageUtils.getTotalPages(totalCount, pageEntity.getPageSize()));
		pageResult.setCurrentPage(pageEntity.getCurrentPage());
		return pageResult;
	}

	private void getUserInfo(List<OrderDetailVo> orderDetailVoList) throws PaasException {
		UserCenter userCenter = null;
		String userId = null;
		UserCenterMapper userCenterMapper = template.getMapper(UserCenterMapper.class);

		if (orderDetailVoList != null && orderDetailVoList.size() > 0) {
			for (int i = 0; i < orderDetailVoList.size(); i++) {
				userId = orderDetailVoList.get(i).getUserId();
				userCenter = userCenterMapper.selectByPrimaryKey(userId);
				if (userCenter == null) {
					throw new PaasException("订单" + orderDetailVoList.get(i).getOrderDetailId() + "的用户信息不存在");
				}
				orderDetailVoList.get(i).setUserOrgName(userCenter.getUserOrgName());
				orderDetailVoList.get(i).setUserEmail(userCenter.getUserEmail());
				orderDetailVoList.get(i).setUserPhoneNum(userCenter.getUserPhoneNum());
				orderDetailVoList.get(i).setUserState(userCenter.getUserState());
				orderDetailVoList.get(i).setUserRegisterTime(userCenter.getUserRegisterTime());
				orderDetailVoList.get(i).setUserActiveTime(userCenter.getUserActiveTime());
				orderDetailVoList.get(i).setUserCancelTime(userCenter.getUserCancelTime());
				orderDetailVoList.get(i).setUserInsideTag(userCenter.getUserInsideTag());
			}
		}
	}

	@Override
	public PageResult<OrderDetailVo> selectOrderList(SelectOrderRequest request) throws PaasException {
		PageEntity pageEntity = request.getPageEntity();

		OrderDetailCriteria orderDetailCriteria = new OrderDetailCriteria();
		OrderDetailCriteria.Criteria criteria = orderDetailCriteria.createCriteria();
		criteria.andUserIdEqualTo(request.getPageEntity().getParams().get("userId"));
		criteria.andProdTypeEqualTo(request.getPageEntity().getParams().get("prodType"));
		orderDetailCriteria.setOrderByClause(" ORDER_APP_DATE DESC");

		OrderDetailMapper orderDetailMapper = template.getMapper(OrderDetailMapper.class);
		int totalCount = orderDetailMapper.countByExample(orderDetailCriteria);

		orderDetailCriteria.setLimitStart(pageEntity.getLimitStart());
		orderDetailCriteria.setLimitEnd(pageEntity.getLimitEnd());

		List<OrderDetail> orderDetails = orderDetailMapper.selectByExampleWithBLOBs(orderDetailCriteria);

		List<OrderDetailVo> orderDetailVoList = new ArrayList<OrderDetailVo>();
		if (orderDetails != null && orderDetails.size() > 0) {
			for (int i = 0; i < orderDetails.size(); i++) {
				OrderDetailVo orderDetailVo = new OrderDetailVo();
				BeanUtils.copyProperties(orderDetails.get(i), orderDetailVo);
				orderDetailVoList.add(orderDetailVo);
			}
			// 查询user信息
			getUserInfo(orderDetailVoList);
		}

		PageResult<OrderDetailVo> pageResult = new PageResult<OrderDetailVo>();
		pageResult.setResultList(orderDetailVoList);
		pageResult.setTotalPages(PageUtils.getTotalPages(totalCount, pageEntity.getPageSize()));
		pageResult.setCurrentPage(pageEntity.getCurrentPage());
		pageResult.setTotalCount(totalCount);

		return pageResult;
	}

	public void updateOrderCheckParam(OrderDetail orderDetail, CheckOrdersRequest request) throws PaasException {
		orderDetail.setOrderCheckDate(DateUtil.getSysDate());
		orderDetail.setOrderCheckDesc(request.getSuggestion());
		orderDetail.setOrderCheckResult(request.getCheckResult());
		orderDetail.setOrderCheckStatus(Constants.Order.OrderCheckStatus.CHECK_STATUS_AUDITED);

		if (Constants.Order.OrderCheckResult.CHECK_PASS.equals(request.getCheckResult())) {
			orderDetail.setOpenResult(Constants.Order.OpenResult.SUCCESS);
			orderDetail.setOpenStatus(Constants.Order.OpenState.PROCESSED);
			orderDetail.setOpenDate(DateUtil.getSysDate());
		} else {
			orderDetail.setOpenResult(Constants.Order.OpenResult.FAIL);
		}
		orderDetail.setOrderStatus(Constants.Order.PaasOrderStatus.ORDER_STATUS_FINISH);

		OrderDetailMapper orderDetailMapper = template.getMapper(OrderDetailMapper.class);
		orderDetailMapper.updateByPrimaryKeySelective(orderDetail);
	}

	public OrderDetail selectByPrimaryKey(long orderDetailId) throws PaasException {
		OrderDetailMapper orderDetailMapper = template.getMapper(OrderDetailMapper.class);
		OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(orderDetailId);
		if (orderDetail == null) {
			throw new PaasException("query orderDetail by order_detail_id failed");
		}
		return orderDetail;
	}

	public long saveUserProdInst(OrderDetail orderDetail, UserProdInst prodInst) throws PaasException {
		UserProdInstMapper mapper = template.getMapper(UserProdInstMapper.class);
		UserProdInst userProdInst = new UserProdInst();
		userProdInst.setUserId(orderDetail.getUserId());
		userProdInst.setUserServType(orderDetail.getProdType());
		userProdInst.setUserServiceId(orderDetail.getProdId());
		userProdInst.setUserServIpaasId(prodInst.getUserServIpaasId());
		userProdInst.setUserServParam(orderDetail.getProdParam());
		userProdInst.setUserProdByname(orderDetail.getProdByname());
		userProdInst.setUserServState(Constants.UserProdInst.UserServState.OPEN);
		userProdInst.setUserServOpenTime(DateUtil.getSysDate());
		userProdInst.setUserServBackParam(prodInst.getUserServBackParam());
		if (Constants.ProdProduct.ProdId.MCS.equals(orderDetail.getProdId())) {
			userProdInst.setUserServRunState(Constants.UserProdInst.UserServRunState.OPEN); // 缓存默认为启动
		}
		mapper.insert(userProdInst);

		return userProdInst.getUserServId();
	}


	public String createUserServIpaasId(OrderDetail orderDetail) throws PaasException {
		UserProdInstMapper mapper = template.getMapper(UserProdInstMapper.class);
		UserProdInstCriteria userProdInstCriteria = new UserProdInstCriteria();
		UserProdInstCriteria.Criteria criteria = userProdInstCriteria.createCriteria();
		criteria.andUserIdEqualTo(orderDetail.getUserId());
		criteria.andUserServiceIdEqualTo(orderDetail.getProdId());
		criteria.andUserProdBynameEqualTo(orderDetail.getProdByname());
		userProdInstCriteria.setOrderByClause(" USER_SERV_IPAAS_ID desc");
		List<UserProdInst> list = mapper.selectByExample(userProdInstCriteria);

		String userServIpaasId = "";
		if ((list == null || list.size() == 0) && !Constants.ProdProduct.ProdId.ATS.equals(orderDetail.getProdId())) {
			userServIpaasId = orderDetail.getProdByname() + Constants.IPAAS_START_ID;
			return userServIpaasId;
		}

		if (Constants.ProdProduct.ProdId.TXS.equals(orderDetail.getProdId())) {
			throw new PaasException("一个用户只允许申请一个事务保障服务");
		}
		if (Constants.ProdProduct.ProdId.ATS.equals(orderDetail.getProdId())) {
			UserProdInstCriteria userProdInstCriteria2 = new UserProdInstCriteria();
			UserProdInstCriteria.Criteria criteria2 = userProdInstCriteria2.createCriteria();
			criteria2.andUserIdEqualTo(orderDetail.getUserId());
			criteria2.andUserServiceIdEqualTo(Constants.ProdProduct.ProdId.TXS);
			criteria2.andUserProdBynameEqualTo(Constants.ProdProduct.ProdEnSimp.TXS);
			List<UserProdInst> txsList = mapper.selectByExample(userProdInstCriteria2);
			if (txsList == null || txsList.size() == 0) {
				throw new PaasException("用户只有在开通事务保障后可以开通最终事物一致服务");
			} else {
				return txsList.get(0).getUserServIpaasId();
			}
		}

		UserProdInst userProdInst = list.get(0);
		String maxserServIpaasId = userProdInst.getUserServIpaasId();
		String curNumStr = maxserServIpaasId.substring(orderDetail.getProdByname().length(),
				maxserServIpaasId.length());
		int nextNum = Integer.parseInt(curNumStr) + 1;
		String nexNumStr = String.valueOf(nextNum);
		String nextNumStr = StringUtil.lPad(nexNumStr, Constants.IPAAS_ID_FIX, Constants.IPAAS_ID_LENGTH);
		userServIpaasId = orderDetail.getProdByname() + nextNumStr;
		logger.info("create servIpaasId：" + userServIpaasId);

		return userServIpaasId;
	}

	public String createServOpenParam(OrderDetail orderDetail) throws PaasException {
		// RDS 拼接参数
		JSONObject rds_prodParamJson = new JSONObject();
		JSONObject prodParamJson = new JSONObject();
		String prodParam = orderDetail.getProdParam();
		logger.info("prodParam :" + prodParam);
		if (StringUtil.isBlank(prodParam)) {
			prodParam = "{}";
		}
		prodParamJson = JsonUtils.parse(orderDetail.getProdParam());
		logger.info("prodParamJson :" + prodParamJson.toString());
		prodParamJson.put("userId", orderDetail.getUserId());
		prodParamJson.put("serviceId", orderDetail.getUserServIpaasId());
		if (!Constants.ProdProduct.ProdId.RDS.equals(orderDetail.getProdId())) {
			prodParamJson.put("applyType", "create");
		} else {
			String createSlaverNum = prodParamJson.get("createSlaverNum").toString();
			rds_prodParamJson.put("instanceBase", prodParamJson);
			rds_prodParamJson.put("createBatmasterNum", 0);
			rds_prodParamJson.put("createSlaverNum", createSlaverNum);

		}
		if (Constants.ProdProduct.ProdId.CCS.equals(orderDetail.getProdId())) {
			prodParamJson.put("timeOut", 2000);
		}
		if (Constants.ProdProduct.ProdId.MDS.equals(orderDetail.getProdId())) {
			String topicEnName = orderDetail.getUserId() + "_" + orderDetail.getUserServIpaasId() + "_"
					+ Math.abs(UUIDTool.genShortId());
			prodParamJson.put("topicEnName", topicEnName);
			// 重写订单产品参数
			orderDetail.setProdParam(prodParamJson.toString());
		}
		if (Constants.ProdProduct.ProdId.DBS.equals(orderDetail.getProdId())) {
			// prodParamJson.put("isNeedSlave", true);
		}
		/*
		 * if(Constants.ProdProduct.ProdId.SES.equals(orderDetail.getProdId())){
		 * prodParamJson.put("replicasNum",1); }
		 */

		if (!Constants.ProdProduct.ProdId.RDS.equals(orderDetail.getProdId())) {
			return prodParamJson.toString();
		} else {
			return rds_prodParamJson.toString();
		}

	}

	public void saveUserMessage(OrderDetail orderDetailTmp) throws PaasException {
		OrderDetail orderDetail = this.selectByPrimaryKey(orderDetailTmp.getOrderDetailId());
		ProdProductMapper prodProductMapper = template.getMapper(ProdProductMapper.class);
		ProdProduct prodProduct = prodProductMapper.selectByPrimaryKey(Short.parseShort(orderDetail.getProdId()));
		if (prodProduct == null) {
			throw new PaasException("根据id查询产品信息为空");
		}
		String content = "";
		if (Constants.Order.OperateType.APPLY.equals(orderDetail.getOperateType())) {
			if (Constants.Order.OrderCheckResult.CHECK_PASS.equals(orderDetail.getOrderCheckResult())) {
				content = "您订购的" + prodProduct.getProdName() + "审核通过";
			} else {
				content = "您订购的" + prodProduct.getProdName() + "审核不通过通过，原因：" + orderDetail.getOrderCheckDesc();
			}
		} else {
			String prodParam = orderDetail.getProdParam();
			@SuppressWarnings("unchecked")
			Map<String, String> map = GsonUtil.fromJSon(prodParam, Map.class);
			if (Constants.Order.OrderCheckResult.CHECK_PASS.equals(orderDetail.getOrderCheckResult())) {
				content = "您对" + prodProduct.getProdName() + map.get("userServIpaasId") + "的扩容申请审核通过";
			} else {
				content = "您对" + prodProduct.getProdName() + map.get("userServIpaasId") + "的扩容申请审核不通过通过，原因："
						+ orderDetail.getOrderCheckDesc();
			}
		}

		UserMessage userMessage = new UserMessage();
		userMessage.setUserId(orderDetail.getUserId());
		userMessage.setUserMsgContent(content);
		userMessage.setUserMsgSendTime(DateUtil.getSysDate());
		UserMessageMapper userMessageMapper = template.getMapper(UserMessageMapper.class);
		userMessageMapper.insert(userMessage);
	}

	public void saveUserMessageUserSure(OrderDetail orderDetailTmp) throws PaasException {
		OrderDetail orderDetail = this.selectByPrimaryKey(orderDetailTmp.getOrderDetailId());
		ProdProductMapper prodProductMapper = template.getMapper(ProdProductMapper.class);
		ProdProduct prodProduct = prodProductMapper.selectByPrimaryKey(Short.parseShort(orderDetail.getProdId()));
		if (prodProduct == null) {
			throw new PaasException("根据id查询产品信息为空");
		}
		String content = "您订购的" + prodProduct.getProdName() + "制定方案成功，需要您确认";
		UserMessage userMessage = new UserMessage();
		userMessage.setUserId(orderDetail.getUserId());
		userMessage.setUserMsgContent(content);
		userMessage.setUserMsgSendTime(DateUtil.getSysDate());
		UserMessageMapper userMessageMapper = template.getMapper(UserMessageMapper.class);
		userMessageMapper.insert(userMessage);
	}

	/**
	 * 待审核提醒邮件，组织邮件内容并返回portal发送。
	 * 
	 * @param orderDetail
	 * @return
	 * @throws PaasException
	 */
	private EmailDetail getAuditPointEmail(OrderDetail orderDetail) throws PaasException {
		UserCenterMapper userCenterMapper = template.getMapper(UserCenterMapper.class);
		UserCenter orderUser = userCenterMapper.selectByPrimaryKey(orderDetail.getUserId());
		if (orderUser == null) {
			throw new PaasException("根据id查询用户信息为空");
		}

		ProdProductMapper prodProductMapper = template.getMapper(ProdProductMapper.class);
		ProdProduct prodProduct = prodProductMapper.selectByPrimaryKey(Short.parseShort(orderDetail.getProdId()));
		if (prodProduct == null) {
			throw new PaasException("根据id查询服务信息为空");
		}

		JSONObject json = new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(orderDetail.getOrderAppDate().getTime());
		json.put("email", orderUser.getUserEmail());
		json.put("appdate", df.format(new Date(orderDetail.getOrderAppDate().getTime())));
		json.put("prodName", prodProduct.getProdName());
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 2);
		String time = df.format(calendar.getTime());
		json.put("limitdate", time);

		String auditUrl = SystemConfigHandler.configMap.get("IPAAS-WEB.SERVICE.IP_PORT_SERVICE")
				+ SystemConfigHandler.configMap.get("AUDIT.AUDIT.URL");
		json.put("auditUrl", auditUrl);

		SysParmRequest request1 = new SysParmRequest();
		request1.setTypeCode("CONTACTS");
		request1.setParamCode("CONTACTS");

		List<SysParamVo> sysParamVoList = iSysParamSv.getSysParams(request1);
		if (null == sysParamVoList || 0 == sysParamVoList.size()) {
			throw new PaasException("参数表未配置联系人信息 ");
		}
		for (int i = 0; i < sysParamVoList.size(); i++) {
			json.put(sysParamVoList.get(i).getServiceValue(), sysParamVoList.get(i).getServiceOption());
		}
		String param = json.toString();

		String subject = "审核提醒";
		String toEmail = json.getString("CONTACTS_EMAIL");
		Map model = GsonUtil.fromJSon(param, Map.class);
		String content = VelocityEngineUtils.mergeTemplateIntoString(EmailTemplUtil.getVelocityEngineInstance(),
				"email/pointAuditMail.vm", "UTF-8", model);
		logger.info("======================邮件模板信息：" + content + "======================");

		try {
			EmailDetail email = new EmailDetail();
			email.setToAddress(toEmail);
			email.setEmailTitle(subject);
			email.setEmailContent(content);

			return email;
		} catch (Exception e) {
			throw new PaasException("获取并组织服务开通的审核通过邮件失败");
		}
	}

}
