package com.github.cryboy007.logger;

import com.github.cryboy007.logger.annotation.LogRecordTest;
import com.github.cryboy007.logger.model.Staff;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.Resource;
import java.util.Collections;

@SpringBootTest
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)//非接口事务代理开启
class LoggerApplicationTests {
	@Resource
	private LogRecordTest logRecordTest;

	@Test
	void contextLoads() {
		Staff staff = new Staff();
		staff.setName("何涛");
		logRecordTest.batchAdd(Collections.singletonList(staff));
	}

}
