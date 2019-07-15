package com.jxc.codeDemo;

import java.util.concurrent.Semaphore;

/**
 * content：
 *      测试 semaphoreDemo
 *      模拟抢车位
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/15
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5, false);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.printf("%s抢到了车位\n", Thread.currentThread().getName());
                    Thread.sleep(3000);
                    System.out.printf("%s 离开了车位\n", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }
}
