package com.ai.paas.ipaas.user.service;

import java.sql.SQLException;

import com.ai.paas.ipaas.vo.user.PageEntity;
import com.ai.paas.ipaas.vo.user.PageResult;
import com.ai.paas.ipaas.vo.user.PlanConfirmVo;


/**
 * 集成方案确认查询
 * @author dingyi5
 *
 */
public interface IPlanConfirmSv {
	public PageResult<PlanConfirmVo> selectPlanConfirm(PageEntity pageEntity) throws SQLException;
	
}
