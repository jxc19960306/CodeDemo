package com.jxc.codeDemo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者 消费者 案例
 * @author Jxc
 * @version 2.0
 * @date 2019/7/17
 */
public class ProducerAndConsumer {
    public static void main(String[] args) {
        User user = new User();
        CountDownLatch countDownLatch = new CountDownLatch(8);
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                countDownLatch.countDown();
                for (int j = 0; j < 10; j++) {
                    user.increment();
                }
            }, "consumer" + i).start();
        }
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                countDownLatch.countDown();
                for (int j = 0; j < 10; j++) {
                    user.decrement();
                }
            }, "producer" + i).start();
        }
        while (Thread.activeCount() > 2) {

        }
        System.out.println(user.getAge());
    }

}

class User {
    private int age;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public int getAge() {
        return age;
    }

    public void increment() {
        lock.lock();
        try {
            while (this.age != 0) {
                condition.await();
            }
            this.age++;
            System.out.println(Thread.currentThread().getName() + "+1");
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    public void decrement() {
        lock.lock();
        try {
            while (this.age == 0) {
                condition.await();
            }
            this.age--;
            System.out.println(Thread.currentThread().getName() + "-1");
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }


    }
}
