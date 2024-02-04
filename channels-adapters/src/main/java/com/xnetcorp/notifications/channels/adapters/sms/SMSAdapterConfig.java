package com.xnetcorp.notifications.channels.adapters.sms;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SMSAdapterConfig {
	@Value("${notification.sms.queue}")
	private String queueName;

	@Value("${notification.sms.routingkey}")
	private String routingKey;
    
	@Bean
  	Binding smsBinding(Queue smsQueue, TopicExchange exchange) {
    	return BindingBuilder.bind(smsQueue).to(exchange).with(routingKey);
  	}

	// SMS RabbitMQ Objects
	@Bean
  	Queue smsQueue() {
    	return new Queue(queueName, true);
  	}

}
