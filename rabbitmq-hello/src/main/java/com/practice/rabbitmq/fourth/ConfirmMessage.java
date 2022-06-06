package com.practice.rabbitmq.fourth;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * 發布確認模式
 * 觀察使用的時間，比較哪種確認方式是最好的
 * <p>
 * 1. 單個確認
 * 2. 批量確認
 * 3. 異步批量確認
 */
public class ConfirmMessage {
    //批量發消息的個數
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1. 單個確認
        publishMessageIndividually(); //發布1000個單獨確認的消息，耗時1739毫秒

        //2. 批量確認
        //3. 異步批量確認
    }

    /**
     * 單個確認
     * @throws Exception
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //隊列聲明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        //開啟發布確認
        channel.confirmSelect();

        //開始時間
        long begin = System.currentTimeMillis();

        //批量發消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes());

            //單個消息就馬上進行發布確認
            boolean flag = channel.waitForConfirms();
            if (flag) System.out.println("消息發送成功");
        }

        //結束時間
        long end = System.currentTimeMillis();

        System.out.println("發布" + MESSAGE_COUNT + "個單獨確認的消息，耗時" + (end - begin) + "毫秒");
    }
}
