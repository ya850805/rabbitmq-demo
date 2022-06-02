package com.practice.rabbitmq.third;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.practice.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手動應答時不丟失，放回隊列中重新消費
 */
public class Worker03 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2等待接收消息處理時間比較長");

        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, (consumerTag, message) -> {
            //沈睡1秒，模擬處理時間
            SleepUtils.sleep(30);

            System.out.println("接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));

            /**
             * 手動應答
             * 1. 消息的標記(tag)，每個消息都有一個唯一標示
             * 2. 是否批量應答，false表示不批量應答信道消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, s -> {
            System.out.println(s + "消費者取消消費接口回調邏輯");
        });
    }
}
