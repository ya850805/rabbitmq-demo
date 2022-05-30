package com.practice.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 連接工廠創建信道的工具類
 */
public class RabbitMqUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        //創建一個連接工廠
        ConnectionFactory factory = new ConnectionFactory();
        //工廠IP，連接rabbitmq隊列
        factory.setHost("localhost");
        //用戶名
        factory.setUsername("admin");
        //密碼
        factory.setPassword("123");

        //創建連接
        Connection connection = factory.newConnection();
        //獲取channel
        Channel channel = connection.createChannel();

        return channel;
    }
}
