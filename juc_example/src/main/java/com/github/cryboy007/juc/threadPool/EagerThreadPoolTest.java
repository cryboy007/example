package com.github.cryboy007.juc.threadPool;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName EagerThreadPoolTest
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/3/30 15:45
 */
public class EagerThreadPoolTest {
    public static void main(String[] args) {
        TaskQueue<Runnable> workQueue = new TaskQueue<>(10);
        EagerThreadPoolExecutor threadPool
                = new EagerThreadPoolExecutor(5,10,1L, TimeUnit.SECONDS,
                workQueue, new ThreadPoolExecutor.CallerRunsPolicy());
        workQueue.setExecutor(threadPool);
        for (int i = 0; i < 15; i++) {
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
}
