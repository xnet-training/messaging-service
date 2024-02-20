package com.xnetcorp.notifications.channels.adapters.webhook;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebhookAdapterConfig {
	@Value("${notification.webhook.queue}")
	private String queueName;

	@Value("${notification.webhook.routingkey}")
	private String routingKey;
    
	@Bean
  	Binding webhookBinding(Queue webhookQueue, TopicExchange exchange) {
    	return BindingBuilder.bind(webhookQueue).to(exchange).with(routingKey);
  	}

	// SMS RabbitMQ Objects
	@Bean
  	Queue webhookQueue() {
    	return new Queue(queueName, true);
  	}    
}
