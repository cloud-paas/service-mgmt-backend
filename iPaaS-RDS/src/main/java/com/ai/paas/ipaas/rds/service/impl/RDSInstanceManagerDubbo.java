package com.ai.paas.ipaas.rds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceManager;
import com.ai.paas.ipaas.rds.service.IRdsSv;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * 传输对象命名规则就是对应方法名
 * 拓扑结构可以随意更改 
 * 空间是通过修改mysql配置和扩充磁盘阵列实现 
 * @author 作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:48:55
 * @version
 * @since
 */
@Service
public class RDSInstanceManagerDubbo implements IRDSInstanceManager {

	@Autowired
	private IRdsSv rdsIncMG;
	
	@Override
	public String create(String createApply) {
		try {
			return rdsIncMG.create(createApply);
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}

	@Override
	public String createslobm(String create) {
		try {
			return rdsIncMG.createslobm(create);
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}

	@Override
	public String cancel(String cancelApply) {
		try {
			return rdsIncMG.cancel(cancelApply);
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}

	@Override
	public String getFuncList() {
		try {
			return rdsIncMG.getFuncList();
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}

	@Override
	public String start(String startApply) {
		try {
			return rdsIncMG.start(startApply);
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}

	@Override
	public String stop(String stopApply) {
		try {
			return rdsIncMG.stop(stopApply);
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}

	@Override
	public String restart(String restartApply) {
		try {
			return rdsIncMG.restart(restartApply);
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}

	@Override
	public String getInstanceInfo(String getinstanceinfo) {
		try {
			return rdsIncMG.getInstanceInfo(getinstanceinfo);
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}

	@Override
	public String modify(String modify) {
		try {
			return rdsIncMG.modify(modify);
		} catch (PaasException e) {
			return e.getErrDetail();
		}
	}
}
