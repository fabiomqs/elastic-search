package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app-config.rabbit.exchange.movie}")
    private String movieTopicExchange;

    @Value("${app-config.rabbit.routingKey.movie}")
    private String movieKey;

    @Value("${app-config.rabbit.queue.movie}")
    private String movieMq;

    @Bean
    public TopicExchange movieTopicExchange() {
        return new TopicExchange(movieTopicExchange);
    }

    @Bean
    public Queue movieMq() {
        return new Queue(movieMq, true);
    }

    @Bean
    public Binding productStockMqBinding(TopicExchange topicExchange) {
        return BindingBuilder
                .bind(movieMq())
                .to(topicExchange)
                .with(movieKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
