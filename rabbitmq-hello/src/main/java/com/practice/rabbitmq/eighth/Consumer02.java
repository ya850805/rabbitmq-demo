package com.practice.rabbitmq.eighth;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信隊列
 * 消費者2
 */
public class Consumer02 {
    //死信交換機名稱
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("等待接收消息...");

        channel.basicConsume(DEAD_QUEUE, true, (consumerTag, message) -> {
            System.out.println("Consumer02接收消息：" + new String(message.getBody(), "UTF-8"));
        }, s -> {
            System.out.println(s + "消費者取消消費接口回調邏輯");
        });
    }
}
