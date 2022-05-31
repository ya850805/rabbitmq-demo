package com.practice.rabbitmq.second;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 這是一個工作線程(消費者)
 */
public class Worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C2等待接收消息...");

        //接收消息
        channel.basicConsume(QUEUE_NAME, true, (s, d) -> {
            //d(第二個參數)為消息
            System.out.println("接收到的消息：" + new String(d.getBody()));
        }, s -> {
            System.out.println("消息消費被中斷：" + s);
        });
    }
}
