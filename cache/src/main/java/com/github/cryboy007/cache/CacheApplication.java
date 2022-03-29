package com.github.cryboy007.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @ClassName CacheApplication
 * @Author tao.he
 * @Since 2022/3/29 17:37
 */
@SpringBootApplication
public class CacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class,args);
    }

    @PostConstruct
    public void initDo() {
    }

}
