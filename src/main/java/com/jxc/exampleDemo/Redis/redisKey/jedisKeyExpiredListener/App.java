package com.jxc.exampleDemo.Redis解决方案.解决redis_key过期监控.jedis_key过期监控;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class App {
        public static void main(String[] args) {
            JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.12.131");

            Jedis jedis = pool.getResource();
            //__keyevent@0__:expired
            jedis.psubscribe(new KeyExpiredListener(), "__key*__:*");

        }

}
