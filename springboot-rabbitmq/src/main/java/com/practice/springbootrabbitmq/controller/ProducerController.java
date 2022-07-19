package com.practice.springbootrabbitmq.controller;

import com.practice.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        log.info("發送消息內容：{}", message);

        /**
         * id設定為1
         */
        CorrelationData correlationData = new CorrelationData("1");
        /**
         * 正確的交換機，發送消息成功
         */
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData);
        /**
         * 錯誤(未存在)的交換機，發送消息失敗
         */
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME + "123", ConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData);

        CorrelationData correlationData2 = new CorrelationData("2");
        /**
         * 錯誤的RoutingKey，發送消息失敗
         */
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY + "xxx", message, correlationData2);
    }
}
