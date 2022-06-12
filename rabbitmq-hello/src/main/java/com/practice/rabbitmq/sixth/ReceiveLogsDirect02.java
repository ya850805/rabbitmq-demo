package com.practice.rabbitmq.sixth;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect02 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = "disk";
        channel.queueDeclare(queueName, true, false, false, null);

        String routingKey = "error";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);

        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect02接收到的消息：" + new String(message.getBody(), "UTF-8"));
        }, s -> {
            System.out.println(s + "消費者取消消費接口回調邏輯");
        });
    }
}
