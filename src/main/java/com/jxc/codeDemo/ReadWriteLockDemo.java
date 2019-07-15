package com.jxc.codeDemo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * content：
 * 读写锁案例 ReentrantReadWriteLock
 * 优点：解决高并发下 读写分离问题，提高并发量
 * * 　    1）支持公平和非公平的获取锁的方式；
 * 　　　　2）支持可重入。读线程在获取了读锁后还可以获取读锁；写线程在获取了写锁之后既可以再次获取写锁又可以获取读锁；
 * 　　　　3）还允许从写入锁降级为读取锁，其实现方式是：先获取写入锁，然后获取读取锁，最后释放写入锁。但是，从读取锁升级到写入锁是不允许的；
 * 　　　　4）读取锁和写入锁都支持锁获取期间的中断；
 * 　　　　5）Condition支持。仅写入锁提供了一个 Conditon 实现；读取锁不支持 Conditon ，readLock().newCondition() 会抛出 UnsupportedOperationException。
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/15
 */
public class ReadWriteLockDemo {
    public static void main(String[] args) throws InterruptedException {
        MyCache myCache = new MyCache();
        //创建 10 个线程去 写入数据
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    myCache.write(j, j);
                }
            }, String.valueOf(i)).start();
        }
        //创建 20 个线程去读取数据
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    myCache.read(j);
                }
            }, String.valueOf(i)).start();
        }
    }
}

class MyCache {
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    volatile private Map map = new HashMap<>();

    public Object read(int key) {
        //加上读锁
        lock.readLock().lock();
        try {
            String name = Thread.currentThread().getName();
            Thread.sleep(200);
            System.out.printf("%s \t正在读取数据\t%s\n", name, key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //加上写锁
            lock.readLock().unlock();
        }
        return map.get(key);
    }

    public void write(int key, Object value) {
        try {
            //加上写锁
            lock.writeLock().lock();
            String name = Thread.currentThread().getName();
            System.out.printf("%s \t正在写入数据\n", name);
            Thread.sleep(200);
            map.put(key, value);
            System.out.printf("%s \t写入数据完成\n", name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //加上读锁
            lock.writeLock().unlock();
        }
    }
}
