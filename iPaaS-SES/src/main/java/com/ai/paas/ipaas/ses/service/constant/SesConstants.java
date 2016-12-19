package com.ai.paas.ipaas.ses.service.constant;

import com.ai.paas.ipaas.PaaSConstant;

public class SesConstants extends PaaSConstant {
	public static final int SES_SERVICE_START = 1;
	public static final int SES_SERVICE_RECYCLE = 1;
	public static final String SPLITER_VERTICAL_LINE = "\\|";
	public static final String SPLITER_COLON = ":";
	public static final String SPLITER_COMMA = ",";
	public static final String SES_ZK_PATH = "/SES/";
	public static final String SES_MAPPING_ZK_PATH = "/SES/MAPPING/";
	public static final int SES_RESOURCE_AVIL = 1;

	public static final String SUCCESS_FLAG = "true";
	public static final String FAIL_FLAG = "false";

	public static final String RESULT_CODE = "resultCode";
	public static final String RESULT_MSG = "resultMsg";
	public static final String SUCCESS_CODE = "000000";
	public static final String FAIL_CODE = "999999";

	public static final int VALIDATE_STATUS = 1;
	public static final int INVALIDATE_STATUS = 2;

	public static final String DDL_KEY = "key";
	public static final String DDL_VALUE = "value";

	public static final String EXPECT_ONE_RECORD_FAIL = "Expect just one record but got nothing or too many results...";
	public static final String RECORD_EXISTS = "record already exists...";
	public static final String SAVE_TO_DB_ERROR = "save to db error...";
	
	/** 1 单库 2 多库、DBS */
	public static final int GROUP_ID_1 = 1;
	public static final int GROUP_ID_2 = 2;
	
	/** 普通数据库 */
	public static final int COMMON_DB_TYPE = 1;
	/** DBS数据库 */
	public static final int DBS_DB_TYPE = 2;
	/** mysql数据库 */
	public static final int MYSQL_DB = 1;
	/** oracle数据库 */
	public static final int ORACLE_DB = 2;

	/** 1主表 */
	public static final int PRIMARY = 1;
	/** 非主表 */
	public static final int IN_PRIMARY = 2;

	public static final String SERVICE_CODE = "ses";

	public static final String IMAGE_CODE = "ses";
	
	public static final String SSH_USER_CODE = "ses_ssh_user";
	
	public static final String SSH_USER_PWD_CODE = "ses_ssh_user_pwd";

	/**
	 * 认证返回结果
	 */
	public static class ExecResult {
		public static final String SUCCESS = "000000"; // 成功
		public static final String FAIL = "999999"; // 失败
		private ExecResult() {
		}
	}
}
