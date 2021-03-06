package com.github.cryboy007.service.impl;

import com.github.cryboy007.event.AfterCommit;
import com.github.cryboy007.event.BeforeCommit;
import com.github.cryboy007.model.Book;
import com.github.cryboy007.service.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName OperationImpl
 * @Author tao.he
 * @Since 2022/1/20 15:40
 */
@Service
@Slf4j
public class OperationImpl implements OperationService {
    @Resource
    private ApplicationEventPublisher publisher;
    @Resource
    private JdbcTemplate jdbcTemplate;

    private final static String sql = "INSERT INTO book (id,title) VALUES (?, ?);";

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void afterCommit() {
        AfterCommit afterCommit = new AfterCommit();
        BeforeCommit beforeCommit = new BeforeCommit();
        final Book book = Book.create();
        publisher.publishEvent(afterCommit);
        jdbcTemplate.update(sql, book.getId(),book.getTitle());
        log.info("事务提交了");
    }

    @Override
    public void transactionTest() {
        final Book book = Book.create();
        jdbcTemplate.update(sql, book.getId(),book.getTitle());
        log.info("事务提交了");
    }

    @Override
    public void transactionTest2() {
        final Book book = Book.create();
        jdbcTemplate.update(sql, book.getId(),book.getTitle());
        log.info("事务提交了");
    }
}
