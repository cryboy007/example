package com.github.cryboy007.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName ReentrantLockDemo
 * @Author tao.he
 * @Since 2022/6/6 13:53
 *
 * 公平锁，就是很公平，在并发环境中，每个线程在获取锁时，会先查看此锁维护的等待队列，如果为空，
 * 或者当前线程就是等待队列的第一个，就占有锁，否则就会加入到等待队列中，以后会按照FIFO的规则从
 * 队列中取到自己
 * ReentrantLock/Synchronized 就是一个典型的可重入锁
 * 对Synchronized而言，是一种非公平锁
 * ReentrantLock 默认是非公平锁
 *
 * 独占锁：指该锁一次只能被一个线程所持有，对ReentrantLock和Synchronized而言都是独占锁
 *
 * 共享锁：只该锁可被多个线程所持有
 * ReentrantReadWriteLock其读锁是共享锁，写锁是独占锁
 *
 * 互斥锁：读锁的共享锁可以保证并发读是非常高效的，读写、写读、写写的过程是互斥的
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        //默认是非公平锁
        Lock lock = new ReentrantLock(true);

        final ReentrantReadWriteLock writeLock = new ReentrantReadWriteLock();
        writeLock.writeLock().lock();
        writeLock.writeLock().unlock();
    }
}
