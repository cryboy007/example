package com.github.cryboy007.juc;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName InterruptedDemo
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2022/11/1 11:15
 */
public class InterruptedDemo {
    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner();
        Thread thread = new Thread(runner);
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
    }

    static class Runner implements Runnable{

        @SneakyThrows
        @Override
        public void run() {
            System.out.println("组装数据");
            int i = 0;
//            TimeUnit.SECONDS.sleep(4);
            while (i < 1000000000) {
                final boolean b = !Thread.currentThread().isInterrupted();
                if (!b) break;
                System.out.println(i);
                i++;
            }

        }
    }

}
