package com.practice.springbootrabbitmq.consumer;

import com.practice.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收消息
 */
@Slf4j
@Component
public class Consumer {
    /**
     * 接收發布確認的消息
     * @param message
     */
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("接收到的隊列confirm.queue消息：{}", msg);
    }
}
