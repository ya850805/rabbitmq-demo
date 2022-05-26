package com.practice.rabbitmq.first;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生產者，主要“發消息”
 */
public class Producer {
    //隊列名稱
    public static final String QUEUE_NAME = "hello";

    //發消息
    public static void main(String[] args) throws IOException, TimeoutException {
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

        /**
         * 生成一個隊列
         * 參數：
         * 1. 隊列名稱
         * 2. 隊列裡面的消息是否需要持久化(存在硬碟)，默認情況下消息存儲在內存中
         * 3. 該隊列是否只供多個消費者進行消費，是否進行消息共享。true: 可以多個消費者消費，false: 只能一個消費者消費
         * 4. 是否自動刪除。最後一個消費者端開連接以後，該隊列是否自動刪除。true: 自動刪除，false: 不自動刪除
         * 5. 其他參數
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //欲發送的消息
        String message = "hello world!!!";

        /**
         * 發送一個消息
         * 1. 發送到哪個交換機(名稱)
         * 2. 路由的key是哪個，本次是隊列名稱
         * 3. 其他參數信息
         * 4. 發送消息體
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("消息發送完畢");
    }
}
