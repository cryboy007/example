package com.github.cryboy007.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName TransactionManagerTest
 * @Author tao.he
 * @Since 2022/5/3 16:48
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionManagerTest {
    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final OperationService operationService;
    private final PlatformTransactionManager platformTransactionManager;

    public void addTwoMethod(boolean isException) {
        transactionTemplate.execute(status -> {
            Object result = null;
            try {
                operationService.transactionTest();
                if (isException){
                    throw new RuntimeException("business is error");
                }
                operationService.transactionTest();
            }catch(Exception e) {
                //回退
                status.setRollbackOnly();
                result = false;
            }
            return result;
        });
    }

    /**
     * 多线程控制
     * @throws InterruptedException
     */
    @SneakyThrows
    public void multiThreadDoMethod() {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        AtomicBoolean isError = new AtomicBoolean();
        List<TransactionStatus> transactionStatusList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                // 创建当前线任务的事务
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                TransactionStatus transaction = this.platformTransactionManager.getTransaction(def);
                transactionStatusList.add(transaction);
                try {
                    countDownLatch.countDown();
                    operationService.transactionTest();
                  /*  if (countDownLatch.getCount() == 3) {
                        throw new RuntimeException("business is error");
                    }*/
                }catch (Exception e) {
                    log.info("error:{0}",e);
                    isError.set(true);
                }
            }).start();
        }
        countDownLatch.await(10, TimeUnit.SECONDS);
        if (isError.get()) {
            //全部回退
            transactionStatusList.forEach(platformTransactionManager::rollback);
        }else {
            //全部提交
            transactionStatusList.forEach(platformTransactionManager::commit);
        }
    }
}
