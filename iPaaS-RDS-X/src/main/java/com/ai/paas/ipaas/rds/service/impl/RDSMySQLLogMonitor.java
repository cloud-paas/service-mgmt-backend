package com.ai.paas.ipaas.rds.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSMySQLLogMonitor;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:51:26 
 * @version 
 * @since  
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RDSMySQLLogMonitor implements IRDSMySQLLogMonitor {

}
