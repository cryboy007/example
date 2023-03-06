package com.github.cryboy007.juc;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName CountDownLatchDemo
 * @Author tao.he
 * @Since 2022/6/6 14:10
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        countDownLatchTest();
    }

    private static void countDownLatchTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"\t被灭");
                //计数器-1
                countDownLatch.countDown();
            }, "ThreadName" + i).start();
        }
        //阻塞
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"\t=====秦统一");
    }
}
