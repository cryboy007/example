package com.github.cryboy007.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName CASDemo
 * @Author tao.he
 * @Since 2022/6/6 11:59
 *
 * 1. ** 循环时间长，开销大**
 * 例如getAndAddInt方法执行，有个do while循环，如果CAS失败，一直会进行尝试，如果CAS长时间不成功，
 * 可能会给CPU带来很大的开销
 * 2. 只能保证一个共享变量的原子操作
 * 对多个共享变量操作时，循环CAS就无法保证操作的原子性，这个时候就可以用锁来保证原子性
 * 3. ABA问题
 */
public class CASDemo {
    public static void main(String[] args) {
        checkCAS();
    }
    //比较当前工作内存中的值和主内存中的值，如果相同则执行规定操作，否则继续比较知道主内存和工作内存中的值一直为止
    private static void checkCAS(){
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5, 2019) + "\t current data is" + atomicInteger.get());
                System.out.println(atomicInteger.compareAndSet(5, 2014) + "\t current data is" + atomicInteger.get());
    }
}
