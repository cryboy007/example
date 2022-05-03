package com.github.cryboy007.logger.service;


import com.github.cryboy007.logger.annotation.LogRecord;

/**
 *@InterfaceName ILogRecordService
 *@Author HETAO
 *@Date 2022/5/1 13:20
 */
public interface ILogRecordService {
	/**
	 * 保存 log
	 *
	 * @param logRecord 日志实体
	 */
	void record(LogRecord logRecord);
}
