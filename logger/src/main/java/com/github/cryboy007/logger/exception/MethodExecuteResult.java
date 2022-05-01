package com.github.cryboy007.logger.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *@ClassName MethodExecuteResult
 *@Author tao.he
 *@Since 2022/4/30 23:47
 */
@Data
@AllArgsConstructor
public class MethodExecuteResult {
	private boolean success;

	private Throwable throwable;

	private String errorMsg;

}
