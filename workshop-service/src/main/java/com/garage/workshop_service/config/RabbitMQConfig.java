package com.garage.workshop_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Notification queue configurations
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.key";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    // Invoice queue configurations
    public static final String INVOICE_EXCHANGE = "invoice.exchange";
    public static final String INVOICE_ROUTING_KEY = "invoice.key";
    public static final String INVOICE_QUEUE = "invoice.queue";

    // Notification exchange and queue beans
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(NOTIFICATION_ROUTING_KEY);
    }

    // Invoice exchange and queue beans
    @Bean
    public TopicExchange invoiceExchange() {
        return new TopicExchange(INVOICE_EXCHANGE);
    }

    @Bean
    public Queue invoiceQueue() {
        return new Queue(INVOICE_QUEUE);
    }

    @Bean
    public Binding invoiceBinding(Queue invoiceQueue, TopicExchange invoiceExchange) {
        return BindingBuilder.bind(invoiceQueue).to(invoiceExchange).with(INVOICE_ROUTING_KEY);
    }
}
