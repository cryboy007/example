package com.tao.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @ClassName TranscationListener
 * @Author tao.he
 * @Since 2022/1/20 15:35
 */
@Slf4j
@Component
public class TransactionListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlerAfterCommit(AfterCommit event) {
        log.info("事务提交后事件");
        doSome();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlerBeforeCommit(BeforeCommit event) {
        log.info("事务未提交");
        doSome();
    }

    public void doSome() {
        log.info("做其它事情");
    }


}
