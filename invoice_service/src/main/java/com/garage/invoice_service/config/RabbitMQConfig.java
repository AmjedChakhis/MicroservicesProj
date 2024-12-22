package com.garage.invoice_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String INVOICE_QUEUE_NAME = "invoice.queue";

    @Bean
    public Queue invoiceQueue() {
        return new Queue(INVOICE_QUEUE_NAME, true); // Durable queue
    }
}
