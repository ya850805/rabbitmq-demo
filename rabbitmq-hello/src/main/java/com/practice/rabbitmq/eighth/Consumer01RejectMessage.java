package com.practice.rabbitmq.eighth;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信隊列
 * 消費者1：聲明普通、死信交換機和隊列
 */
public class Consumer01RejectMessage {
    //普通交換機名稱
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交換機名稱
    public static final String DEAD_EXCHANGE = "dead_exchange";

    //普通隊列名稱
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信隊列名稱
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        //聲明普通和死信交換機，類型為direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //聲明普通隊列
        Map<String, Object> arguments = new HashMap<>();
        //正常隊列設置死信交換機
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //設置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //**設置正常隊列的長度限制**
//        arguments.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, true, false, false, arguments);

        //聲明死信隊列
        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);

        //綁定普通、死信交換機和隊列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息...");

        //消費消息，需要開啟手動應答(第二個參數為false)
        channel.basicConsume(NORMAL_QUEUE, false, (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");

            if("info5".equals(msg)) {
                System.out.println("Consumer01接收消息：" + msg + "，此消息被C1拒絕");

                //第二個參數代表是否再放回正常隊列
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01接收消息：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        }, s -> {
            System.out.println(s + "消費者取消消費接口回調邏輯");
        });
    }
}
