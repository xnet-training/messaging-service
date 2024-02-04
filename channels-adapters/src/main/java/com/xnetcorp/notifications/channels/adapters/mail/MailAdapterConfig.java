package com.xnetcorp.notifications.channels.adapters.mail;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailAdapterConfig {
	@Value("${notification.mail.queue}")
	private String queueName;

	@Value("${notification.mail.routingkey}")
	private String routingKey;
    
	@Bean
  	Binding mailBinding(Queue mailQueue, TopicExchange exchange) {
    	return BindingBuilder.bind(mailQueue).to(exchange).with(routingKey);
  	}

	// SMS RabbitMQ Objects
	@Bean
  	Queue mailQueue() {
    	return new Queue(queueName, true);
  	}

}
