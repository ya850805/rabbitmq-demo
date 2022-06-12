package com.practice.rabbitmq.sixth;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        //聲明一個交換機
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //聲明一個隊列
        String queueName = "console";
        channel.queueDeclare(queueName, true, false, false, null);

        //綁定交換機與隊列
        String routingKey1 = "info";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey1);
        String routingKey2 = "warning";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey2);

        //接收消息
        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01接收到的消息：" + new String(message.getBody(), "UTF-8"));
        }, s -> {
            System.out.println(s + "消費者取消消費接口回調邏輯");
        });
    }
}
