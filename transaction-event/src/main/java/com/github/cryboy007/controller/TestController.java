package com.github.cryboy007.controller;

import com.github.cryboy007.message.Result;
import com.github.cryboy007.service.OperationService;
import com.github.cryboy007.service.TransactionManagerTest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Author tao.he
 * @Since 2022/5/3 16:28
 */
@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {
    private final OperationService operationService;
    private final TransactionManagerTest transactionManagerTest;

    @GetMapping("eventTest")
    public Result<?> eventTest() {
        operationService.afterCommit();
        return Result.success();
    }

    @GetMapping("programmingTransactionManagerTest")
    public Result<?> programmingTransactionManagerTest() {
        transactionManagerTest.addTwoMethod(true);
        return Result.success();
    }

    @GetMapping("multiThreadDoMethod")
    public Result<?> multiThreadDoMethod() {
        transactionManagerTest.multiThreadDoMethod();
        return Result.success();
    }
}
