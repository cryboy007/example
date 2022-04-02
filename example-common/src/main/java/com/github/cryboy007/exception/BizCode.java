package com.github.cryboy007.exception;

public enum BizCode implements ICode {

	SUCCESS("100000","操作成功"),
	TOKEN_ERROR("200000","token失效"),
	SECURITY_ERROR("300000","访问权限不足"),SECURITY_FREQ("300001","访问频次异常"),SECURITY_BLACK("300003","黑名单用户"),
	INVALID_ARGS("400000","缺少必要的参数"),
	INVALID_SQL("400001","SQL 语法异常"),
	SERVER_ERROR("5000000","系统异常"),SERVER_REMOTE_ERROR("6000001","远程业务系统异常"),
	IDEMPONT_INVALID("600000","无效的幂等态"),
	IDEMPONT_PASS("600001","幂等记录已存在"),
	MULTIPLE_RECORDS("700001","One record is expected, but the query result is multiple records"),
	CACHE_INIT_ERROR("700002","缓存初始化失败")
	;
	
	
	private String code;
	
	private String message;

	public String getCode() {
		return code;
	}


	public String getMessage() {
		return message;
	}


	private BizCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
