package com.practice.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 隊列TTL消費者
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {
    //接收消息
    @RabbitListener(queues = {"QD"})
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("當前時間：{}，收到死信隊列的消息：{}", LocalDateTime.now(), msg);
    }
}
