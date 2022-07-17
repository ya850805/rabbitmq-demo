package com.practice.springbootrabbitmq.consumer;

import com.practice.springbootrabbitmq.config.DelayedQueueConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 消費者 基於插件的延遲消息
 */
@Slf4j
@Component
public class DelayQueueConsumer {
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("當前時間：{}，收到死信隊列的消息：{}", LocalDateTime.now(), msg);
    }
}
