package com.github.cryboy007.juc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName ReadWriteLockDemo
 * @Author tao.he
 * @Since 2022/6/6 14:00
 * 读写锁
 */
public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        for (int i = 1; i <= 5; i++) {
            final int tempInt = i;
            new Thread(() -> {
                myCache.put(tempInt + "", tempInt + "");
            }, "Thread " + i).start();
        }
        for (int i = 1; i <= 5; i++) {
            final int tempInt = i;
            new Thread(() -> {
                myCache.get(tempInt + "");
            }, "Thread " + i).start();
        }
    }
}

class MyCache {
    private volatile Map<String, Object> map = new HashMap<>();
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
/** 写操作：原子+独占
 * 整个过程必须是一个完整的统一体，中间不许被分割，不许被打断
 *
 * @param key
 * @param value
 */
void put(String key, Object value) {
    rwLock.writeLock().lock();
    try {
        System.out.println(Thread.currentThread().getName() + "\t正在写入：" +
                key);
        TimeUnit.MILLISECONDS.sleep(300);
        map.put(key, value);
        System.out.println(Thread.currentThread().getName() + "\t写入完成");
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        rwLock.writeLock().unlock();
    }
}
    void get(String key) {
        rwLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t正在读取：" +
                    key);
            TimeUnit.MILLISECONDS.sleep(300);
            Object result = map.get(key);
            System.out.println(Thread.currentThread().getName() + "\t读取完成: " +
                    result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rwLock.readLock().unlock();
        }
    }
    public void clear() {
        map.clear();
    }
}
