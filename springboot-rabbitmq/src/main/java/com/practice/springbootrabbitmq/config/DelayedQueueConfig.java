package com.practice.springbootrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayedQueueConfig {
    //交換機
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //隊列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //routing-key
    public static final String DELAYED_ROUTING_KEY = "delayed_routingKey";

    //隊列
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //基於插件的交換機
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        //延遲類型是direct，代表直接發到Queue不是路由
        arguments.put("x-delayed-type", "direct");

        /**
         * 建構子參數：
         * 1. 交換機名稱
         * 2. 交換機類型
         * 3. 是否需要持久化
         * 4. 是否需要自動刪除
         * 5. 其他參數
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    //綁定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                      @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
