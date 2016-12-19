package com.ai.paas.ipaas.ses.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ses.dao.interfaces.SesUserInstanceMapper;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserInstance;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserInstanceCriteria;
import com.ai.paas.ipaas.ses.service.ISesUserInst;
import com.ai.paas.ipaas.ses.service.constant.SesConstants;

@Service
@Transactional(rollbackFor = Exception.class)
public class SesUserInstImpl implements ISesUserInst {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(SesUserInstImpl.class);

	@Override
	public SesUserInstance queryInst(String userId, String srvId)
			throws PaasException {
		List<SesUserInstance> resultList;
		try {
			SesUserInstanceMapper mapper = ServiceUtil
					.getMapper(SesUserInstanceMapper.class);
			SesUserInstanceCriteria example = new SesUserInstanceCriteria();
			example.createCriteria().andUserIdEqualTo(userId)
					.andServiceIdEqualTo(srvId);
			resultList = mapper.selectByExample(example);
			if (resultList != null && resultList.size() > 1) {
				return resultList.get(0);
			} else {
				throw new PaasException(SesConstants.ExecResult.FAIL,
						SesConstants.EXPECT_ONE_RECORD_FAIL);
			}
		} catch (Exception e) {
			LOGGER.error(SesConstants.ExecResult.FAIL, e);
			throw new PaasException(SesConstants.ExecResult.FAIL, e);
		}
	}

}
