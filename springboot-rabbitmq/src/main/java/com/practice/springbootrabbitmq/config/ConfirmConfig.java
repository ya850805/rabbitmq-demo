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

    //備份交換機
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    //備份隊列
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    //報警隊列
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    @Bean
    public DirectExchange confirmExchange() {
        //確認交換機綁定備份交換機
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true).withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmExchangeBindingConfirmQueue(@Qualifier("confirmExchange") DirectExchange confirmExchange, @Qualifier("confirmQueue") Queue confirmQueue) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backupExchangeBindingBackupQueue(@Qualifier("backupExchange") FanoutExchange backupExchange, @Qualifier("backupQueue") Queue backupQueue) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding backupExchangeBindingWarningQueue(@Qualifier("backupExchange") FanoutExchange backupExchange, @Qualifier("warningQueue") Queue warningQueue) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
