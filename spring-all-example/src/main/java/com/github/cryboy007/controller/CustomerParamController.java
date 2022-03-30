package com.github.cryboy007.controller;

import com.github.cryboy007.annotation.CustomerParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CustomerParamController
 * @Author tao.he
 * @Since 2022/3/30 16:40
 */
@RestController
@Slf4j
public class CustomerParamController {

    @GetMapping("argumentResolver")
    public void argumentResolver(@CustomerParam(key = "demo") String demo) {
        log.info("demo:{}",demo);
    }
}
