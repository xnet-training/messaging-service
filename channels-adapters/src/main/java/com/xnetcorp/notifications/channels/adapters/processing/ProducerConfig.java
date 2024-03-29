package com.xnetcorp.notifications.channels.adapters.processing;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {
  	@Value("${notification.businessevent.queue}")
	private String queueName;

	@Value("${notification.businessevent.routingkey}")
	private String routingKey;
    
	@Bean
  	Binding businessEventBinding(Queue businessEventQueue, TopicExchange exchange) {
    	return BindingBuilder.bind(businessEventQueue).to(exchange).with(routingKey);
  	}

	@Bean
  	Queue businessEventQueue() {
    	return new Queue(queueName, true);
  	}

	@Bean
    public VelocityEngine velocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "string");
		velocityEngine.setProperty("resource.loader.string.class", StringResourceLoader.class.getName());
		velocityEngine.setProperty("resource.loader.string.cache", true);
		velocityEngine.setProperty("resource.loader.string.modification_check_interval", 60);
        //velocityEngine.setProperty("resource.loader.class.class", ClasspathResourceLoader.class.getName());
        //velocityEngine.init();
		return velocityEngine;
    }

}
