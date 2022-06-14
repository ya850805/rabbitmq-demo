package com.practice.rabbitmq.seventh;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生產者
 */
public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "被隊列Q1Q2接收到");
        bindingKeyMap.put("lazy.orange.elephant", "被隊列Q1Q2接收到");
        bindingKeyMap.put("quick.orange.fox", "被隊列Q1接收到");
        bindingKeyMap.put("lazy.brown.fox", "被隊列Q2接收到");
        bindingKeyMap.put("lazy.pink.rabbit", "雖然滿足兩個綁定但只會被Q2接收一次");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何綁定不會被任何隊列接收到，會被丟棄");
        bindingKeyMap.put("quick.orange.male.rabbit", "是四個單詞不匹配任何綁定會被丟棄");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是四個單詞會被Q2接收到");

        for (Map.Entry<String, String> entry : bindingKeyMap.entrySet()) {
            String routingKey = entry.getKey();
            String message = entry.getValue();

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println("生產者發出消息：" + entry.getValue());
        }
    }
}
