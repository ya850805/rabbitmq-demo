package com.practice.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 發送延遲消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        log.info("當前時間：{}，發送一條信息給兩個TTL隊列：{}", LocalDateTime.now(), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息來自TTL為10秒的隊列" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息來自TTL為40秒的隊列" + message);
    }
}
