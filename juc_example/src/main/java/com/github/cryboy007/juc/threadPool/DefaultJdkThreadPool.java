package com.github.cryboy007.juc.threadPool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DefaultJdkThreadPool
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/3/30 15:04
 *
 * 默认JDK线程池  满核心线程数,先放队列  队列不够在去创建新的线程
 */
public class DefaultJdkThreadPool {
    public static void main(String[] args) {
//        ThreadPoolExecutor threadPool = defaultConfiguration();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5,5, 1L,
                TimeUnit.SECONDS,new LinkedBlockingDeque<>(10));
        threadPool.allowCoreThreadTimeOut(true);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threadPool.execute(thread);
        }
        threadPool.shutdown();
    }

    private static ThreadPoolExecutor defaultConfiguration() {
        return new ThreadPoolExecutor(5,10, 10L,
                TimeUnit.SECONDS,new LinkedBlockingDeque<>(20));
    }
}
