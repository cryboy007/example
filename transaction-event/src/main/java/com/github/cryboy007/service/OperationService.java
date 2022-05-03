package com.github.cryboy007.service;

/**
 * @ClassName OperationService
 * @Author tao.he
 * @Since 2022/1/20 15:40
 */
public interface OperationService {
    void afterCommit();

    void transactionTest();

    void transactionTest2();
}
