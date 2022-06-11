package com.practice.rabbitmq.fifth;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息接收
 */
public class ReceiveLogs02 {

    //交換機的名稱
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //聲明一個交換機
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /**
         * 聲明一個臨時隊列，隊列的名稱是隨機的
         * 當消費者斷開與隊列的連接時，隊列就會自動刪除
         *
         */
        String queueName = channel.queueDeclare().getQueue();

        /**
         * 綁定交換機與隊列
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把接收到的消息打印出來...");

        /**
         * 接收消息
         */
        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("02接收到的消息：" + new String(message.getBody(), "UTF-8"));
        }, s -> {
            System.out.println(s + "消費者取消消費接口回調邏輯");
        });
    }
}
