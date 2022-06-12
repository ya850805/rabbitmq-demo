package com.practice.rabbitmq.sixth;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class EmitLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner sc = new Scanner(System.in);

        //ReceiveLogsDirect01接收
//        String routingKey = "info";
//        String routingKey = "warning";

        //ReceiveLogsDirect02接收
        String routingKey = "error";

        while (sc.hasNext()) {
            String message = sc.next();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("EmitLogs發送消息：" + message);
        }
    }
}
