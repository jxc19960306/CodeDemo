package com.jxc.codeDemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * content：
 *      测试BlockingQueueDemo
 *      add: java.lang.IllegalStateException: Queue full
 *      remove: java.util.NoSuchElementException
 *      offer: false
 *      poll: null
 *      put: wait
 *      take: wait
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/15
 */
public class BlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue(3);
        //队列满了后 会报错 java.lang.IllegalStateException: Queue full
        blockingQueue.add(2);
        //队列满了后 会阻塞
        blockingQueue.put(1);
        //队列满了后 返回 false
        blockingQueue.offer(3);
        //队列空了后 会报错 java.util.NoSuchElementException
        blockingQueue.remove();
        //队列空了后 会返回 null
        blockingQueue.poll();
        //队列空了后 会阻塞
        blockingQueue.take();

        BlockingQueue blockingQueue1 = new SynchronousQueue();
        new Thread(() -> {
            try {
                System.out.println("第一次 插入数据");
                blockingQueue1.put(1);
                System.out.println("第二次 插入数据");
                blockingQueue1.put(2);
                System.out.println("第三次 插入数据");
                blockingQueue1.put(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "1").start();

        new Thread(() -> {
            try {
                Object take = blockingQueue1.take();
                System.out.printf("第一次读取数据, 读取到的数据是：%s\n", take);
                Thread.sleep(2000);
                Object take2 = blockingQueue1.take();
                System.out.printf("第二次读取数据, 读取到的数据是：%s\n", take2);
                Thread.sleep(2000);
                Object take3 = blockingQueue1.take();
                System.out.printf("第三次读取数据, 读取到的数据是：%s\n", take3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
