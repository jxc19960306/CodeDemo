package com.jxc.codeDemo;

import java.util.concurrent.atomic.AtomicReference;

/**
 * content：
 *      手写 自旋锁案例
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/15
 */
public class MyLock {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock() {
        Thread thread = Thread.currentThread();
        while (!atomicReference.compareAndSet(null, thread)) {

        }
    }

    public void unLock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);

    }

    public static void main(String[] args) throws InterruptedException {
        MyLock myLock = new MyLock();
        new Thread(() -> {
            myLock.lock();
            try {
                //睡眠 5s ,方便测试 第二个线程 是否能够拿到锁
                System.out.println("come in !!!");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myLock.unLock();
        }).start();
        //睡眠 1s 保证第一个线程 优先执行
        Thread.sleep(1000);
        new Thread(() -> {
            myLock.lock();
            System.out.println("I'am Thread Two");
            myLock.unLock();
        }).start();

    }
}
