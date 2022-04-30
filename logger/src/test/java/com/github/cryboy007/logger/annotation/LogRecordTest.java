package com.github.cryboy007.logger.annotation;

import com.github.cryboy007.logger.enums.LoggerTemplate;
import com.github.cryboy007.logger.model.Staff;

import org.springframework.stereotype.Component;

/**
 *@ClassName LogRecordTest
 *@Author tao.he
 *@Since 2022/4/30 17:02
 */
@Component
public class LogRecordTest {
	@LogRecord(template = LoggerTemplate.RECORD_OPERATOR,spelValue = {"#staff.name","新增"})
	public void operationAdd(Staff staff) {
		System.out.println(staff);
	}
}
