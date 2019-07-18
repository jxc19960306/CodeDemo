package com.jxc.codeDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * content：
 * 多个线程按顺序调用，实现 A -> B -> C 三个线程启动，要求如下：
 * AA 打印 5次， BB 打印 10次， CC 打印 15次
 * AA 打印 5次， BB 打印 10次， CC 打印 15次
 * 。。。。
 * 循环 10次
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/17
 */
public class ProducerAndConsumer2 {
    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    shareResource.pint5();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    shareResource.pint10();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    shareResource.pint15();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();
    }

}

class ShareResource {
    // 1 A 2 B 3 C
     int flag = 1;
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    public void pint5() throws InterruptedException {
        try {
            lock.lock();
            while (flag != 1) {
                condition1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.printf("%s----%s%n", Thread.currentThread().getName(), i);
            }
            flag = 2;
            System.out.println("flag = " + flag);
            condition2.signal();
        } finally {
            lock.unlock();
        }
    }

    public void pint10() throws InterruptedException {
        try {
            lock.lock();
            while (flag != 2) {
                condition2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.printf("%s----%s%n", Thread.currentThread().getName(), i);
            }
            flag = 3;
            System.out.println("flag = " + flag);
            condition3.signal();
        } finally {
            lock.unlock();
        }
    }

    public void pint15() throws InterruptedException {
        try {
            lock.lock();
            while (flag != 3) {
                condition3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.printf("%s----%s%n", Thread.currentThread().getName(), i);
            }
            flag = 1;
            System.out.println("flag = " + flag);
            condition1.signal();
        } finally {
            lock.unlock();
        }
    }
}
