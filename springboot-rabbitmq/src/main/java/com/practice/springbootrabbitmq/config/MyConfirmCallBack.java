package com.practice.springbootrabbitmq.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 回調接口
 */
@Slf4j
@Component
public class MyConfirmCallBack implements RabbitTemplate.ConfirmCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        //注入RabbitTemplate的內部類ConfirmCallback
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 交換機確認回調方法
     * 1. 發消息，交換機成功接收消息，回調
     *      1.1. correlationData：保存回調消息的ID及相關信息
     *      1.2. ack：交換機是否收到消息(成功時就是true)
     *      1.3. cause：null(成功時沒有失敗原因)
     *
     * 2. 發消息，交換機接收消息失敗，回調
     *      1.1. correlationData：保存回調消息的ID及相關信息
     *      1.2. ack：交換機是否收到消息(失敗時就是false)
     *      1.3. cause：失敗原因
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = Objects.isNull(correlationData)? "" : correlationData.getId();
        if(ack) {
            log.info("交換機成功接收消息，Id為：{}的消息", id);
        } else {
            log.info("交換機收到Id為：{}的消息失敗，原因：{}", id, cause);
        }
    }
}
