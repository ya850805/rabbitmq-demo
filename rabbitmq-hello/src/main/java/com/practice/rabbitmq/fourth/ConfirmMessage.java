package com.practice.rabbitmq.fourth;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;

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
//        publishMessageIndividually(); //發布1000個單獨確認的消息，耗時1739毫秒

        //2. 批量確認
//        publishMessageBatch(); //發布1000個批量確認的消息，耗時125毫秒

        //3. 異步批量確認
        publishMessageAsync(); //發布1000個異步發布的消息，耗時111毫秒
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

    /**
     * 批量發布確認
     * @throws Exception
     */
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        //批量確認消息大小
        int batchSize = 100;

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes());

            //判斷達到100條消息時，批量確認一次
            if((i + 1) % 100 == 0) {
                channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("發布" + MESSAGE_COUNT + "個批量確認的消息，耗時" + (end - begin) + "毫秒");
    }

    /**
     * 異步發布確認
     * @throws Exception
     */
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.confirmSelect();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);

        /**
         * 消息確認成功 回調函數
         * 1. 消息的標記
         * 2. 是否為批量確認
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("確認的消息：" + deliveryTag);
        };

        /**
         * 消息確認失敗 回調函數
         * 1. 消息的標記
         * 2. 是否為批量確認
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未確認的消息：" + deliveryTag);
        };

        /**
         * 監聽器，監聽哪些消息成功了，哪些消息失敗了(成功/失敗由broker通知)
         * 1. 監聽哪些消息成功了
         * 2. 監聽哪些消息失敗了
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes());
        }

        long end = System.currentTimeMillis();

        System.out.println("發布" + MESSAGE_COUNT + "個異步發布的消息，耗時" + (end - begin) + "毫秒");
    }
}
