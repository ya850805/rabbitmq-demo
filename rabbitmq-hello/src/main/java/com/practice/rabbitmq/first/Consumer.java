package com.practice.rabbitmq.first;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消費者，主要“接收消息”
 */
public class Consumer {
    //隊列的名稱(與生產者那邊相同)
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //創建一個連接工廠
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("123");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        /**
         * 消費消息
         * 1. 消費哪個隊列(隊列名稱)
         * 2. 消費成功之後是否要自動應答。true: 自動應答，false: 手動應答
         * 3. 未成功消費的回調
         * 4. 消費者取消消費的回調
         */
        channel.basicConsume(QUEUE_NAME, true, (s, d) -> {
            //d(第二個參數)為消息
            System.out.println(new String(d.getBody()));
        }, s -> {
            System.out.println("消息消費被中斷");
        });

    }
}
