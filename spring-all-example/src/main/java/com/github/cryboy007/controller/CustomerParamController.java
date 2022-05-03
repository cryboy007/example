package com.github.cryboy007.controller;

import com.github.cryboy007.annotation.CustomerParam;
import com.github.cryboy007.model.Book;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("httpMessageConverter")
    public ResponseEntity<Book> httpMessageConverter(@RequestBody Book book) {
        log.info("book--{}",book);
        return ResponseEntity.ok(book);
    }
}
