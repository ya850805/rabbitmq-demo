package com.practice.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 發布確認 配置類
 */
@Configuration
public class ConfirmConfig {
    //交換機
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    //隊列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    //RoutingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";

    @Bean
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmExchangeBindingConfirmQueue(@Qualifier("confirmExchange") DirectExchange confirmExchange, @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }
}
