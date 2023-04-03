package com.github.cryboy007.juc.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @ClassName ParallelStreamTest
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2023/3/30 16:12
 */
public class ParallelStreamTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(String.format("  >>> 电脑 CPU 并行处理线程数 :: %s, 并行流公共线程池内线程数 :: %s",
                Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.commonPool().getParallelism()));
        List<String> stringList = new ArrayList<>();
        List<String> stringList2 = new ArrayList<>();
        for (int i = 0; i < 13; i++) stringList.add(String.valueOf(i));
        for (int i = 0; i < 3; i++) stringList2.add(String.valueOf(i));

        new Thread(() -> {
            stringList.parallelStream().forEach(each -> {
                System.out.println(Thread.currentThread().getName() + " 开始执行 :: " + each);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }, "子线程-1").start();

        Thread.sleep(1500);

        new Thread(() -> {
            stringList2.parallelStream().forEach(each -> {
                System.out.println(Thread.currentThread().getName() + " :: " + each);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }, "子线程-2").start();
    }

//
//    public static void main(String[] args) {
////        CopyOnWriteArrayList<Integer> integerList = new CopyOnWriteArrayList<>();
//        List<Integer> integerList = new ArrayList<>();
//        List<String> strList = new ArrayList<>();
//
//        int practicalSize = 100000;
//
//        for (int i = 0; i < practicalSize; i++) {
//            strList.add(String.valueOf(i));
//        }
//
//        strList.parallelStream().forEach(each -> {
//            integerList.add(Integer.parseInt(each));
//        });
//
//        System.out.println("  >>> integerList 预计长度 :: {}"+ practicalSize);
//        System.out.println("  >>> integerList 实际长度 :: {}"+ integerList.size());
//    }
}
