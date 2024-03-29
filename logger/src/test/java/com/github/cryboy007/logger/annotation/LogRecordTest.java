package com.github.cryboy007.logger.annotation;

import com.github.cryboy007.logger.enums.LoggerTemplate;
import com.github.cryboy007.logger.model.Staff;
import com.github.cryboy007.logger.resolver.LogRecordContext;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *@ClassName LogRecordTest
 *@Author tao.he
 *@Since 2022/4/30 17:02
 */
@Component
public class LogRecordTest {
	@LogRecord(template = LoggerTemplate.RECORD_OPERATOR,spelValue = {"#staff.name","#operator","新增"},bizNo = "40001")
	public void operationAdd(Staff staff) {
		LogRecordContext.putVariable("oldDeliveryUserId", "1001");
		System.out.println("operationAdd");
		currentProxy().operationDel(staff);
	}

	@LogRecord(template = LoggerTemplate.RECORD_OPERATOR,spelValue = {"#staff.name","#operator","删除"},bizNo = "40003")
	public void operationDel(Staff staff) {
		System.out.println("operationDel");
		currentProxy().operationQuery(staff);
	}

	@LogRecord(template = LoggerTemplate.RECORD_OPERATOR,spelValue = {"#staff.name","#operator","查询"},bizNo = "40002")
	public void operationQuery(Staff staff) {
		System.out.println("operationQuery");
	}

	private LogRecordTest currentProxy() {
		return (LogRecordTest)AopContext.currentProxy();
	}

	@LogRecord(template = LoggerTemplate.RECORD_OPERATOR,spelValue = {"#staffs[0].name","#operator","批量"},bizNo = "40000")
	public void batchAdd(List<Staff> staffs) {
		currentProxy().operationQuery(staffs.get(0));

	}

	@LogRecord(template = LoggerTemplate.RECORD_OPERATOR2,bizNo = "40002")
	public void operationQuery2(Staff staff) {
		System.out.println("operationQuery");
	}
}
