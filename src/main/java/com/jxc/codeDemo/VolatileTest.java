package com.jxc.codeDemo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试 volatile 不保证原子性
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/13
 */
public class VolatileTest {
    public static void main(String[] args) {
        Demo1 demo1 = new Demo1();
        //创建20个线程
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    demo1.addPulsPlus();
                }
            }, String.valueOf(i)).start();
        }

        //主线程，监控 demo1 的变量是否改变
        while (Thread.activeCount() > 2) {

        }
        System.out.println("期望值是" + 20 * 1000);
        System.out.println("主线程监控到 demo1 的变量已经改变，当前变量值为" + demo1.a);
        System.out.println("主线程监控到 atomicInteger 的变量已经改变，当前变量值为" + demo1.atomicInteger);
    }

}

class Demo1 {
    //使用了 volatile 修饰
    volatile int a = 0;
    AtomicInteger atomicInteger = new AtomicInteger();

    public void addPulsPlus() {
        this.a++;
        atomicInteger.getAndIncrement();
    }
}