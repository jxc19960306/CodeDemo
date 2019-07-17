package com.jxc.exampleDemo.rocketMq.rocketMq_Simple_Demo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
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
public class RocketMqProducer {
    public static void main(String[] args) throws Exception {
        RocketMqProducer rocketMqProducer = new RocketMqProducer();
        rocketMqProducer.sendOneway();
    }

    /**
     * 同步发送消息
     * 可靠的同步传输，用于重要通知，短信通知，短信营销系统
     */
    @Test
    public void synchronousSendMessage() throws Exception {
        //1、创建 默认的 rocketMq 生产者, 组名 随意
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup1");
        //2、指定 名称服务器
        producer.setNamesrvAddr("192.168.12.131:9876");
        //3、启动 生产者
        producer.start();

        //4、创建 要发送的消息  param1: 主题  param2:标签 （子主题）  param3: 要发送的内容，字节数组
        Message message = new Message("demo7", "tag1", "hello RocketMQ".getBytes(Charset.defaultCharset()));

        //5、发送信息
        SendResult sendResult = producer.send(message);

        //6、输出返回结果
        System.out.printf("%s%n", sendResult);

        //7、关闭生产者
        producer.shutdown();
    }

    /**
     * 异步发送信息
     * 用于响应时间敏感的业务场景
     */
    @Test
    public void asynchronousSendMessage() throws Exception {
        //1、创建 默认的 rocketMq 生产者, 组名 随意
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup1");
        //2、指定 名称服务器
        producer.setNamesrvAddr("192.168.12.131:9876");
        //3、启动 生产者
        producer.start();
        //4、设定 发送异步失败时重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);
        //5、创建 要发送的消息  param1: 主题  param2:标签 （子主题）  param3: 要发送的内容，字节数组
        Message message = new Message("demo1", "tag1", "hello RocketMQ".getBytes(Charset.defaultCharset()));
        //6、发送信息
        producer.send(message, new SendCallback() {
            /** 消费成功调用这个方法 */
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.printf("%s%n", sendResult.getMsgId());
            }

            /** 消费失败调用这个方法 */
            @Override
            public void onException(Throwable throwable) {
                //打印异常信息
                throwable.printStackTrace();
            }
        });

        //7、关闭提供者
        producer.shutdown();
    }

    /**
     * 单向发送信息
     */
    @Test
    public void sendOneway() throws Exception {
        //1、创建 默认的 rocketMq 生产者, 组名 随意
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup1");
        //2、指定 名称服务器
        producer.setNamesrvAddr("192.168.12.131:9876");
        //3、启动 生产者
        producer.start();
        //4、创建 要发送的消息  param1: 主题  param2:标签 （子主题）  param3: 要发送的内容，字节数组
        Message message = new Message("demo1", "tag1", "hello RocketMQ".getBytes(Charset.defaultCharset()));
        //5、以单向模式发送信息
        producer.sendOneway(message);
        //6、关闭提供者
        producer.shutdown();
    }

    /**
     * 使用 FIFO 发送信息
     */
    @Test
    public void orderSendMessage() throws Exception {
        //1、创建 默认的 rocketMq 生产者, 组名 随意
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup1");
        //2、指定 名称服务器
        producer.setNamesrvAddr("192.168.12.131:9876");
        //3、启动 生产者
        producer.start();
        //4、创建 要发送的消息  param1: 主题  param2:标签 （子主题）  param3: 要发送的内容，字节数组
        Message message = new Message("demo1", "tag1", "hello RocketMQ".getBytes(Charset.defaultCharset()));
        //5、以单向模式发送信息
        producer.send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                return list.get(0);
            }
        }, 1);
        //6、关闭提供者
        producer.shutdown();
    }

    /**
     * 广播发送信息
     * @throws Exception
     */
  /*  @Test
    public void broadcastProduct() throws Exception{
        //1、创建 默认的 rocketMq 生产者, 组名 随意
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup1");
        //2、指定 名称服务器
        producer.setNamesrvAddr("192.168.12.131:9876");
        //3、启动 生产者
        producer.start();
        //4、创建 要发送的消息  param1: 主题  param2:标签 （子主题）  param3: 要发送的内容，字节数组
        Message message = new Message("demo1", "tag1", "hello RocketMQ".getBytes(Charset.defaultCharset()));

    }*/
}
