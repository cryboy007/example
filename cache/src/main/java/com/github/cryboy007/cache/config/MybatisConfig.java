package com.github.cryboy007.cache.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.cryboy007.cache.inteceptor.NoArgsWhereHelper;
import com.github.cryboy007.cache.inteceptor.NoArgsWhereInterceptor;
import com.github.cryboy007.utils.ServiceUtils;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @ClassName MybatisConfig
 * @Author tao.he
 * @Since 2022/4/6 11:48
 */
@Configuration
@Slf4j
public class MybatisConfig {

    private final static String DEFAULT_DATASOURCE_PRFIX = "spring.datasource";

   /* @Primary
    @Bean(name = "default_db")
    @ConfigurationProperties(DEFAULT_DATASOURCE_PRFIX)
    public DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        ((HikariDataSource) dataSource).setJdbcUrl(ServiceUtils.getPropertyByKey(DEFAULT_DATASOURCE_PRFIX + ".url"));
        return dataSource;
    }*/

    @Bean
    @Primary
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSource defaultDB, NoArgsWhereInterceptor noArgsWhereInterceptor) throws IOException {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(defaultDB);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.addInterceptor(noArgsWhereInterceptor);
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        try {
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        } catch (Exception e) {
            log.error("not have mapper file!");
        }

        GlobalConfig globalConfig = new GlobalConfig();
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);

        return sqlSessionFactoryBean;
    }

}
