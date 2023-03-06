package com.github.cryboy007.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName VolatileDemo
 * @Author tao.he
 * @Since 2022/6/6 11:39
 * 1验证volatile的可见性
 * 1.1 如果int num = 0，number变量没有添加volatile关键字修饰
 * 1.2 添加了volatile，可以解决可见性
 *  2.验证volatile不保证原子性
 *  2.1 原子性指的是什么
 * 不可分割、完整性，即某个线程正在做某个具体业务时，中间不可以被加塞或者被分割，需要整体完
 * 整，要么同时成功，要么同时失败
 */
public class VolatileDemo {
    public static void main(String[] args) {
//        visibilityByVolatile();//验证volatile的可见性
        atomicByVolatile();//验证volatile不保证原子性
    }

    /**
     * volatile可以保证可见性，及时通知其他线程，主物理内存的值已经被修改
     */
    public static void visibilityByVolatile() {
        MyData myData = new MyData();
//第一个线程
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
//线程暂停3s
                TimeUnit.SECONDS.sleep(3);
                //赋值
                myData.addToSixty();
                System.out.println(Thread.currentThread().getName() + "\t updatevalue:" + myData.num);
            } catch (Exception e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }, "thread1").start();
//第二个线程是main线程
        while (myData.num == 0) {
//如果myData的num一直为零，main线程一直在这里循环
        }
        System.out.println(Thread.currentThread().getName() + "\t mission is over,num value is " + myData.num);
    }


    /**
     * volatile不保证原子性
     * 以及使用Atomic保证原子性
     */
    public static void atomicByVolatile(){
        MyData myData = new MyData();
        for(int i = 1; i <= 20; i++){
            new Thread(() ->{
                for(int j = 1; j <= 1000; j++){
                    myData.addSelf();
                    myData.atomicAddSelf();
                }
            },"Thread "+i).start();
        }
//等待上面的线程都计算完成后，再用main线程取得最终结果值
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (Thread.activeCount()>2){
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+"\t finally num valueis "+myData.num);
                System.out.println(Thread.currentThread().getName()+"\t finally atomicnumvalue is "+myData.atomicInteger);
    }
}

class MyData {
     //int num = 0;
    // volatile可以保证可见性，及时通知其他线程，主物理内存的值已经被修改
    volatile int num = 0;
    public void addToSixty() {
        this.num = 60;
    }

    public void addSelf(){
        num++;
    }

    public void atomicAddSelf(){
        atomicInteger.getAndIncrement();
    }
    AtomicInteger atomicInteger = new AtomicInteger();
}
