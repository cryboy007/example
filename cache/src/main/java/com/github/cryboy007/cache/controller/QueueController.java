package com.github.cryboy007.cache.controller;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName QueueController
 * @Author tao.he
 * @Since 2022/4/2 19:13
 */
@RestController
public class QueueController {
    private static final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
    private AtomicInteger i = new AtomicInteger();
    @SneakyThrows
    @GetMapping("upload")
    public ResponseEntity<String> upload(){
        queue.put("1"+ (i.getAndIncrement()));

        return ResponseEntity.ok("ok");
    }
    @PostConstruct
    public void doSome(){
        new Thread(() -> {
            while (true) {
                String take = null;
                try {
                    take = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("============"+take);
                System.out.println("开始上传文件");
                System.out.println("释放资源");
            }
        }).start();
    }
}
