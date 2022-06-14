package com.practice.rabbitmq.seventh;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 聲明主題交換機及相關隊列
 */
public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        //聲明交換機
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //聲明隊列
        String queueName = "Q1";
        channel.queueDeclare(queueName, true, false, false, null);

        //綁定交換機和隊列
        String routingKey = "*.orange.*";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);

        System.out.println("等待接收消息");

        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("ReceiveLogsTopic01接收消息：" + new String(message.getBody(), "UTF-8") + ", 接收隊列：" + queueName + ", 綁定鍵：" + message.getEnvelope().getRoutingKey());
        }, s -> {
            System.out.println(s + "消費者取消消費接口回調邏輯");
        });
    }
}
