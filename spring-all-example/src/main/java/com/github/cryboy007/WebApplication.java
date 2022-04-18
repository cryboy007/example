package com.github.cryboy007;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @ClassName WebApplication
 * @Author tao.he
 * @Since 2022/3/30 11:01
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class,args);
    }

}
