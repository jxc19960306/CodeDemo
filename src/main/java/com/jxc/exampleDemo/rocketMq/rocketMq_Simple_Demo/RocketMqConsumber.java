package com.jxc.exampleDemo.rocketMq.rocketMq_Simple_Demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Project: export_parent
 * Role：
 *
 * @author Jxc
 * @version 1.0
 * @date 2019/7/16
 */
@SuppressWarnings("all")
public class RocketMqConsumber {
    public static void main(String[] args) throws Exception {
        RocketMqConsumber rocketMqConsumber = new RocketMqConsumber();
        rocketMqConsumber.concurrentListerMessage();
    }

    /**
     * 并发读取信息，无序
     *
     * @throws Exception
     */
    @Test
    public void concurrentListerMessage() throws Exception {
        //1、创建 默认的 rocketMq 消费者, 组名 随意 ，实现类 DefaultMQPullConsumer ，DefaultMQPushConsumer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("producerGroup1");
        //2、指定 名称服务器
        consumer.setNamesrvAddr("192.168.12.131:9876");
        //3、consumer 订阅要消费的主题 和 标签(所有 标签 用 * 表示)
        consumer.subscribe("demo7", "tag1");
        //4、consumer 注册消息监听器 MessageListenerConcurrently: 监听并发信息，读取无序
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt messageExt : list) {
                    try {
                        //取出 收到的消息
                        String result = new String(messageExt.getBody(), Charset.defaultCharset());
                        //输出 信息
                        System.out.printf("%s", result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //响应失败, 会重现读取再次 消费
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                //响应成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //3、启动 消费者
        consumer.start();
    }


    /**
     * 有序读取队列信息
     *
     * @throws Exception
     */
    @Test
    public void orderListerMessage() throws Exception {
        //1、创建 默认的 rocketMq 消费者, 组名 随意 ，实现类 DefaultMQPullConsumer ，DefaultMQPushConsumer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("producerGroup1");
        //2、指定 消费者读取消息的顺序
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //3、指定 名称服务器
        consumer.setNamesrvAddr("192.168.12.131:9876");
        //4、consumer 订阅要消费的主题 和 标签(所有 标签 用 * 表示)
        consumer.subscribe("demo1", "tag1");
        //5、consumer 注册消息监听器 MessageListenerOrderly: 监听队列信息 有序
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                for (MessageExt messageExt : list) {
                    try {
                        System.out.printf("%s%n", messageExt);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        //6、启动消费者
        consumer.start();
    }

    /**
     * 广播订阅消息
     */
    @Test
    public void broadcastConsumer() throws Exception{
        //1、创建 默认的 rocketMq 消费者, 组名 随意 ，实现类 DefaultMQPullConsumer ，DefaultMQPushConsumer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("producerGroup1");
        //2、指定 名称服务器
        consumer.setNamesrvAddr("192.168.12.131:9876");
        //3、设定 广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
        //4、consumer 订阅要消费的主题 和 标签(所有 标签 用 * 表示, 或用  ||)
        consumer.subscribe("demo1", "tag1");
        //5、consumer 注册消息监听器 MessageListenerConcurrently: 监听并发信息，读取无序
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt messageExt : list) {
                    try {
                        //取出 收到的消息
                        String result = String.valueOf(messageExt.getBody());
                        //输出 信息
                        System.out.printf("%s%n", result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //响应失败, 会重现读取再次 消费
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                //响应成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //6、启动 消费者
        consumer.start();
    }
}
