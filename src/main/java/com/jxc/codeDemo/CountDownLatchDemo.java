package com.jxc.codeDemo;

import java.util.concurrent.CountDownLatch;

/**
 * Project: CountDownLatchDemo
 * content：
 * 测试 CountDownLatch
 *          CountDownLatch是通过“共享锁”实现的。在创建CountDownLatch中时，会传递一个int类型参数count，
 *      该参数是“锁计数器”的初始状态，表示该“共享锁”最多能被count给线程同时获取。当某线程调用该
 *      CountDownLatch对象的await()方法时，该线程会等待“共享锁”可用时，才能获取“共享锁”进而继续运行。
 *      而“共享锁”可用的条件，就是“锁计数器”的值为0！而“锁计数器”的初始值为count，每当一个线程调用该
 *      CountDownLatch对象的countDown()方法时，才将“锁计数器”-1；通过这种方式，必须有count个线程调用
 *      countDown()之后，“锁计数器”才为0，而前面提到的等待线程才能继续运行！
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/15
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        new Thread(() -> {
            try {
                countDownLatch.await();
                System.out.println("辅导员离开了教室");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.printf("%s 同学离开了教室\n", Thread.currentThread().getName());
                //每离开一位同学 计数器就减一
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        //等待 计数器变为 0 然后才执行后面的代码
        countDownLatch.await();
        System.out.println("班长离开教室，然后锁门");

    }
}
