package com.github.cryboy007.message;

import com.github.cryboy007.utils.TurnDate;
import com.github.cryboy007.utils.UuidUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auth 统一返回包装类
 * @param <T>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@ApiModel(value = "返回参数说明")
@Data
public class Result<T> implements Serializable {
	private static final long serialVersionUID = 1084094914898698049L;
	private final static String DEFAULT_ERROR_CODE = "999999";
	private final static String DEFAULT_SUCCESS_CODE = "100000";
	@ApiModelProperty(value = "请求id")
	private String requestId;
	@ApiModelProperty(value = "请求编码，code为0代表的是成功，其他都是失败！")
	private String code;
	@ApiModelProperty(value = "返回的提示信息")
	private String msg;
	@ApiModelProperty(value = "true 代表的是返回成功，false代表的是返回失败！")
	private boolean success;
	private T data;
	@ApiModelProperty(value = "请求的时间戳")
	private String timestamp;

	private Result() {
	}

	/**
	 * 这边得到最终的组装的结果集
	 */
	private Result(String requestId, String code, String msg, T data) {
		this.requestId = requestId;
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.timestamp = TurnDate.getStringDate();
	}

	public static <T> Result<T> error() {
		return error(DEFAULT_ERROR_CODE, "未知异常，请联系管理员");
	}

	public static <T> Result<T> error(String msg) {
		return error(DEFAULT_ERROR_CODE, msg);
	}

	public static <T> Result<T> error(String code, String msg) {
		String requestId = UuidUtil.getUUID();
		Result result = new Result(requestId, code, msg, null);
		result.setSuccess(false);
		return result;
	}

	public static <T> Result<T> error(String code, T data, String msg) {
		String requestId = UuidUtil.getUUID();
		String timestamp = TurnDate.getStringDate();
		Result result = new Result(requestId, code, msg, data);
		result.setSuccess(false);
		return result;
	}

	public static <T> Result<T> success() {
		return (Result<T>) success((Object) null, "操作成功！");
	}

	public static <T> Result<T> success(T data) {
		return success(data, "操作成功！");
	}

	/**
	 * @deprecated 使用success(T data, String msg)
	 */
	public static <T> Result<T> success(String msg) {
		return (Result<T>) success((Object) null, msg);
	}

	public static <T> Result<T> success(T data, String msg) {
		String requestId = UuidUtil.getUUID();
		String timestamp = TurnDate.getStringDate();
		Result result = new Result(requestId, DEFAULT_SUCCESS_CODE, msg, data);
		result.setSuccess(true);
		return result;
	}
}
