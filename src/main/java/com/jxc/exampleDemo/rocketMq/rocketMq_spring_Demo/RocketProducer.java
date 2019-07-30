package com.jxc.exampleDemo.rocketMq.rocketMq_spring_Demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Project: export_parent
 * Role：
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/17
 */
public class RocketProducer {
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Test
    public void sendMessage() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        defaultMQProducer.send(
                new Message("topic","tags1",new byte[]{1,2}));
    }
}
