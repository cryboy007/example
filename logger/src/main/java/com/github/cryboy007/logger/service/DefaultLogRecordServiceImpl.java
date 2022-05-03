package com.github.cryboy007.logger.service;

import com.github.cryboy007.logger.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;

/**
 *@ClassName DefaultLogRecordServiceImpl
 *@Author tao.he
 *@Since 2022/5/1 13:21
 */
@Slf4j
public class DefaultLogRecordServiceImpl implements ILogRecordService{
	@Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void record(LogRecord logRecord) {
		log.info("【logRecord】log={}", logRecord);
	}
}
