package com.jxc.exampleDemo.Redis解决方案.解决redis_key过期监控.jedis_key过期监控;

import redis.clients.jedis.JedisPubSub;

/**
 * jedis 实现监听 redis key 过期事件
 */
public class KeyExpiredListener extends JedisPubSub {


    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPSubscribe "
                + pattern + " " + subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

        System.out.println("onPMessage pattern "
                + pattern + " " + channel + " " + message);
    }

}
