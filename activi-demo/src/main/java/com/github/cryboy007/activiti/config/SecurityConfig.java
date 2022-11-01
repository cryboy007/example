package com.github.cryboy007.activiti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security框架配置
 */
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //省略HttpSecurity的配置
        httpSecurity
                // 关闭csrf防护
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()

                //定制url访问权限
                .authorizeRequests()

                //无限登录即可访问
                .antMatchers("/**").permitAll()

                //需要特定权限
//                .antMatchers("/sysUser/**","/sysAuthority/**").hasAnyAuthority("ROLE_ADMIN","ROLE_SA")

                //其他接口登录才能访问
//                .anyRequest().authenticated()
                .and();
        return httpSecurity.build();
    }

}
