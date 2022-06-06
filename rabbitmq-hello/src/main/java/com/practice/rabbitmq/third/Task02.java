package com.practice.rabbitmq.third;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手動應答時不丟失，放回隊列中重新消費
 */
public class Task02 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        //開啟發布確認
        channel.confirmSelect();

        //聲明隊列，持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        //從控制台輸入消息
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String message = sc.next();
            //設置生產者發送消息為持久化消息(要求保存到磁盤上，一般存在內存)
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生產者發出消息：" + message);
        }
    }
}
