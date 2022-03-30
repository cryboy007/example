package com.github.cryboy007.test;

import com.github.cryboy007.lua.Limit;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试限流
 *
 */
@Component
public class TestLimit implements ApplicationRunner {
    @Resource
    private Limit limit;
    AtomicInteger cnt = new AtomicInteger(0);
    public void invoked() {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        if (!limit.limit("limit", 5, 1000)) {
            System.out.println(now + ": 我是执行了,当前cnt=" + cnt.incrementAndGet());
        }
    }
    @Override
    public void run(ApplicationArguments args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.scheduleAtFixedRate(this::invoked, 0, 10, TimeUnit.MILLISECONDS);
        }
    }
}
