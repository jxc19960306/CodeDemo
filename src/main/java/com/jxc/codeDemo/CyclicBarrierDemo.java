package com.jxc.codeDemo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * content：
 *      CyclicBarrier Demo
 *      和 CountDowLatch 使用刚好相反， CountDownLatch 是将计数减少为 0 时，取消阻塞
 *      CyclicBarrier 是 当计数器加到指定 数字时 取消阻塞
 * @author Jxc
 * @version 1.0
 * @date 2019/7/15
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("集齐 7 颗龙珠，召唤神龙");
        });
        for (int i = 0; i < 7; i++) {
            new Thread(() -> {
                try {
                    System.out.printf("%s 号线程收集到了一颗龙珠\n", Thread.currentThread().getName());
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(i)).start();
        }
    }
}
