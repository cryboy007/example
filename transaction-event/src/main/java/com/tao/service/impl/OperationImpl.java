package com.tao.service.impl;

import com.tao.event.AfterCommit;
import com.tao.event.BeforeCommit;
import com.tao.service.OperationService;
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

    private final static String sql = "INSERT INTO city (NAME, state, country) VALUES (?, ?, ?);";

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void afterCommit() {
        AfterCommit afterCommit = new AfterCommit();
        BeforeCommit beforeCommit = new BeforeCommit();
        publisher.publishEvent(afterCommit);
        jdbcTemplate.update(sql,new String[]{"a","b","c"});
        log.info("事务提交了");
    }

}
