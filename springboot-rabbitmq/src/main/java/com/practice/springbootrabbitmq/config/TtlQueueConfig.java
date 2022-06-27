package com.practice.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TTL隊列，配置文件代碼
 */
@Configuration
public class TtlQueueConfig {
    //普通交換機的名稱
    public static final String X_EXCHANGE = "X";
    //死信交換機的名稱
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通隊列名稱
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信隊列名稱
    public static final String QUEUE_DEAD_LETTER = "QD";

    //普通隊列，適用所有TTL消息
    public static final String QUEUE_C = "QC";

    //聲明X交換機
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    //聲明Y交換機
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //聲明隊列 TTL為10s
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>(3);
        //設置死信交換機
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //設置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //設置TTL，單位為ms
        arguments.put("x-message-ttl", 10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //聲明隊列 TTL為40s
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>(3);
        //設置死信交換機
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //設置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //設置TTL，單位為ms
        arguments.put("x-message-ttl", 40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //死信隊列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(QUEUE_DEAD_LETTER).build();
    }

    //綁定 隊列A和交換機X
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //綁定 隊列B和交換機X
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>();
        //設置死信交換機
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //設置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");

        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
}
