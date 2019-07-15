package com.jxc.codeDemo;

/**
 * content：
 *      测试 可重入锁（又名递归锁）案例
 * @author Jxc
 * @version 1.0
 * @date 2019/7/15
 */
public class ReentrantLockTest {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() ->{
            phone.sendEmail();
        }, "t2").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() ->{
            phone.sendSMS();
        },"t1").start();
    }
}

class Phone{
    public synchronized void sendSMS(){
        System.out.printf("%s 调用了sendSMS 方法\n",Thread.currentThread().getName());
        sendEmail();
    }

    public synchronized void sendEmail(){
        String name = Thread.currentThread().getName();
        if (name.equals("t2")){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("%s 调用了 sendEmail 方法 \n", name);
    }
}
