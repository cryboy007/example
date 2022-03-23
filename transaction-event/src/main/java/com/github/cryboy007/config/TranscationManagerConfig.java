package com.github.cryboy007.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @ClassName TranscationManagerConfig
 * @Author tao.he
 * @Since 2022/1/20 15:30
 */
@Configuration
@EnableTransactionManagement //开启注解支持
public class TranscationManagerConfig implements TransactionManagementConfigurer {

    @Resource(name="dataSourceTransactionManager")
    private PlatformTransactionManager dataSourceTransactionManager;

    @Bean(name = "dataSourceTransactionManager")
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return dataSourceTransactionManager;
    }
}
