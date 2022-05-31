package com.practice.rabbitmq.second;

import com.practice.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生產者，發送大量消息
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 生成一個隊列
         * 參數：
         * 1. 隊列名稱
         * 2. 隊列裡面的消息是否需要持久化(存在硬碟)，默認情況下消息存儲在內存中
         * 3. 該隊列是否只供多個消費者進行消費，是否進行消息共享。true: 可以多個消費者消費，false: 只能一個消費者消費
         * 4. 是否自動刪除。最後一個消費者端開連接以後，該隊列是否自動刪除。true: 自動刪除，false: 不自動刪除
         * 5. 其他參數
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //從console接收消息並發送
        Scanner sc = new Scanner(System.in);

        while(sc.hasNext()) {
            String message = sc.next();

            /**
             * 發送一個消息
             * 1. 發送到哪個交換機(名稱)
             * 2. 路由的key是哪個，本次是隊列名稱
             * 3. 其他參數信息
             * 4. 發送消息體
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("發送消息完成：" + message);
        }
    }
}
