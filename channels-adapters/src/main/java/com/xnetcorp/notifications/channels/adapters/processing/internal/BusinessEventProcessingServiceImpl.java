package com.xnetcorp.notifications.channels.adapters.processing.internal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.xnetcorp.notifications.channels.adapters.NotificationMessage;
import com.xnetcorp.notifications.channels.adapters.processing.BusinessEventMessage;
import com.xnetcorp.notifications.channels.adapters.processing.BusinessEventProcessingService;
import com.xnetcorp.notifications.channels.adapters.subscriptions.SubscriptionService;
import com.xnetcorp.notifications.channels.adapters.subscriptions.model.SubscriptionChannel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BusinessEventProcessingServiceImpl implements BusinessEventProcessingService {

    @Autowired
    private SubscriptionService subscriptionService;
    
    @Value("${notification.exchange}")
    private String exchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VelocityEngine velocityEngine;

    @Override
    public void execute(BusinessEventMessage message) {
        log.info("[Evento de Negocio]" + message.getEventType());

        List<SubscriptionChannel> channels = 
            this.subscriptionService.findSubscriptionsChannels(message.getOwnerId(), message.getEventType());
        
        Optional.of(channels).orElse(new ArrayList<>()).parallelStream().forEach(channel -> {
            NotificationMessage msg = new NotificationMessage();
            msg.setProperties(new HashMap<>());
            msg.setChannel( channel.getChanelName() );

            String messageTemplate = (String)message.getPayload().get("message");
            //velocityEngine.set.setTemplate( message );
            msg.setMessage( messageTemplate );
            msg.getProperties().putAll( channel.getProperties() );

            String channelName = channel.getChanelName().toLowerCase();
            String routingKey = "queue.".concat(channelName);
            rabbitTemplate.convertAndSend(exchange, routingKey, msg);
            log.info("[Evento de Negocio] Despachado a " + channelName);
        });
        
    }
    
}
