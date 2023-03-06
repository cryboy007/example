package com.github.cryboy007.juc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName Delayquue2Demo
 * @Author tao.he
 * @email study.hetao@foxmail.com
 * @Since 2022/12/29 15:17
 *
 * 元素进入队列后，先进行排序，然后，只有getDelay也就是剩余时间为0的时候，该元素才有资格被消费者从队列中取出来，所以构造函数一般都有一个时间传入。
 */
public class Delayquue2Demo {
    public static void main(String[] args) throws Exception {
        BlockingQueue<Task> delayqueue = new DelayQueue<>();
        long now = System.currentTimeMillis();
        delayqueue.put(new Task(now+3000));
        delayqueue.put(new Task(now+4000));
        delayqueue.put(new Task(now+6000));
        delayqueue.put(new Task(now+1000));
        System.out.println(delayqueue);

        for(int i=0; i<4; i++) {
            System.out.println(delayqueue.take());
        }

    }

    static class Task implements Delayed{
        long time = System.currentTimeMillis();
        public Task(long time) {
            this.time = time;
        }
        @Override
        public int compareTo(Delayed o) {
            if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS))
                return -1;
            else if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS))
                return 1;
            else
                return 0;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }
        @Override
        public String toString() {
            return "" + time;
        }
    }
}
