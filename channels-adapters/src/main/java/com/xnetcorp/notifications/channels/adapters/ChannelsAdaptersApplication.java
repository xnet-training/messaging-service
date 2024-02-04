package com.xnetcorp.notifications.channels.adapters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class ChannelsAdaptersApplication {

	@Value("${notification.exchange}")
	private String topicExchangeName;


	public static void main(String[] args) {
		SpringApplication.run(ChannelsAdaptersApplication.class, args);
	}

	@Bean
  	TopicExchange exchange() {
    	return new TopicExchange(topicExchangeName);
  	}

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
  
	/*@Bean
  	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    	SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
    	container.setQueueNames(queueName);
    	container.setMessageListener(listenerAdapter);
    	return container;
  	}

  	@Bean
  	MessageListenerAdapter listenerAdapter(RabbitMQConsumer receiver) {
    	return new MessageListenerAdapter(receiver, "receiveMessage");
  	}*/
}
