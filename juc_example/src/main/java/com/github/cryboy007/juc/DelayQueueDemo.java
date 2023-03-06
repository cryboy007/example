package com.github.cryboy007.juc;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DeadQueueDemo
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2022/12/29 14:59
 *
 */
public class DelayQueueDemo {
    private static DelayQueue<MyDelayedTask> delayQueue  = new DelayQueue<MyDelayedTask>();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                delayQueue.offer(new MyDelayedTask("task1",10000));
                delayQueue.offer(new MyDelayedTask("task2",3900));
                delayQueue.offer(new MyDelayedTask("task3",1900));
                delayQueue.offer(new MyDelayedTask("task4",5900));
                delayQueue.offer(new MyDelayedTask("task5",6900));
                delayQueue.offer(new MyDelayedTask("task6",7900));
                delayQueue.offer(new MyDelayedTask("task7",4900));

            }
        }).start();
        Delayed take = null;
        while (true) {
            try {
                take = delayQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(take);
        }
    }

    static class MyDelayedTask implements Delayed {

        private String name ;
        private long start = System.currentTimeMillis();
        private long time ;

        public MyDelayedTask(String name,long time) {
            this.name = name;
            this.time = time;
        }
        /**
         * 需要实现的接口，获得延迟时间   用过期时间-当前时间
         * @param unit
         * @return
         */
        @Override
        public long getDelay(TimeUnit unit) {
            long convert = unit.convert((start + time) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
//            System.out.println("getDelay-----" + convert);
            return convert;
        }
        /**
         * 用于延迟队列内部比较排序   当前时间的延迟时间 - 比较对象的延迟时间
         * @param o
         * @return
         */
        @Override
        public int compareTo(Delayed o) {
//            MyDelayedTask o1 = (MyDelayedTask) o;
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "MyDelayedTask{" +
                    "name='" + name + '\'' +
                    ", start=" + start +
                    ", time=" + time +
                    '}';
        }
    }
}
