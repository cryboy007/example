package com.github.cryboy007.exception;

/**
 * 
 * 服务业务异常
 * 
 * @author LeoChan
 *
 */
public class BizException extends RuntimeException {

	private static final long serialVersionUID = -6044349980837906219L;

	private String code;

	private String errMsg;

	/** 异常数据对象，拦截器会将data赋值到Result的data中 */
	private Object data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public BizException() {
		super();
	}

	public BizException(String code, String errMsg) {
		super(errMsg);
		this.code = code;
		this.errMsg = errMsg;
	}

	public BizException(String code, String errMsg,Object data) {
		super(errMsg);
		this.code = code;
		this.errMsg = errMsg;
		this.data = data;
	}

	public BizException(ICode code) {
		super(code.getMessage());
		this.code = code.getCode();
		this.errMsg = code.getMessage();
	}

	public BizException(ICode code, String... args) {
		super(code.getMessage());
		this.code = code.getCode();
		this.errMsg = code.getErrorMessage(args);
	}
}
