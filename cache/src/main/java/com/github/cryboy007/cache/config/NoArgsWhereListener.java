package com.github.cryboy007.cache.config;

import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.github.cryboy007.cache.inteceptor.NoArgsWhereInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
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
public class NoArgsWhereListener implements ApplicationListener<ContextStartedEvent> {

    private final NoArgsWhereInterceptor noArgsWhereInterceptor;
    private final List<SqlSessionFactory> sqlSessionFactoryList;

    public NoArgsWhereListener(NoArgsWhereInterceptor noArgsWhereListener, List<SqlSessionFactory> sqlSessionFactoryList) {
        this.noArgsWhereInterceptor = noArgsWhereListener;
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        log.debug("添加自定义Mybatis SQL拦截器........");
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(noArgsWhereInterceptor);
        }
    }
}
