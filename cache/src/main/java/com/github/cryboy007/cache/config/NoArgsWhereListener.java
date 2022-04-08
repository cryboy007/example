package com.github.cryboy007.cache.config;

import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.github.cryboy007.cache.inteceptor.NoArgsWhereInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lei.mao
 * @since 2021/6/30
 */
@Slf4j
@Component
public class NoArgsWhereListener implements ApplicationListener<ApplicationStartedEvent> {

    private final NoArgsWhereInterceptor noArgsWhereInterceptor;
    private final List<SqlSessionFactory> sqlSessionFactoryList;

    public NoArgsWhereListener(NoArgsWhereInterceptor noArgsWhereListener, List<SqlSessionFactory> sqlSessionFactoryList) {
        this.noArgsWhereInterceptor = noArgsWhereListener;
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.debug("添加自定义Mybatis SQL拦截器........");
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(noArgsWhereInterceptor);
        }
        /*log.info("+++++++++++++++++++++++++++++++++++++++++++++");
        if (event instanceof ContextStartedEvent){
            log.info("================:{}", "ContextStartedEvent");
        }
        if (event instanceof ContextRefreshedEvent){
            log.info("================:{}", "ContextRefreshedEvent");
        }
        if (event instanceof ContextClosedEvent){
            log.info("================:{}", "ContextClosedEvent");
        }
        if (event instanceof ContextStoppedEvent){
            log.info("================:{}", "ContextStoppedEvent");
        }
        if (event instanceof ApplicationReadyEvent){
            log.info("================:{}", "ApplicationReadyEvent");
        }*/
        log.info(">>>>>>>>>>>>>>>>:{}\n", event.getClass().getName());
    }
}
