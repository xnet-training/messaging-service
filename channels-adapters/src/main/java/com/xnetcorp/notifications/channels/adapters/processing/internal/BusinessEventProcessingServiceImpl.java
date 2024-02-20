package com.xnetcorp.notifications.channels.adapters.processing.internal;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        velocityEngine.init(); 

        List<SubscriptionChannel> channels = 
            this.subscriptionService.findSubscriptionsChannels(message.getOwnerId(), message.getEventType());
        
        Optional.of(channels).orElse(new ArrayList<>()).parallelStream().forEach(channel -> {
            VelocityContext context = new VelocityContext();
            context.put("message", message);
            context.put("channel", channel);

            NotificationMessage msg = new NotificationMessage();
            msg.setProperties(new HashMap<>());
            msg.getProperties().putAll( channel.getProperties() );
            channel.getIgnoresProperties().stream().forEach(k -> msg.getProperties().remove( k ));
            msg.setChannel( channel.getChanelName() );

            StringResourceRepository repository = StringResourceLoader.getRepository();

            String messageTemplate = (String)channel.getProperties().get("message");
            if(messageTemplate != null && !"".equals(messageTemplate)) {            
                repository.putStringResource("message_template", messageTemplate);

                StringWriter msgWriter = new StringWriter();
                this.velocityEngine.getTemplate("message_template").merge(context, msgWriter);
                msg.setMessage( msgWriter.toString() );
            }

            String subjectTemplate = (String)channel.getProperties().get("subject");
            if(subjectTemplate != null && !"".equals(subjectTemplate)) {
                repository.putStringResource("subject_template", subjectTemplate);
                StringWriter subjWriter = new StringWriter();
                this.velocityEngine.getTemplate("subject_template").merge(context, subjWriter);
                msg.getProperties().put("subject", subjWriter.toString());
            }

            String channelName = channel.getChanelName().toLowerCase();
            String routingKey = "queue.".concat(channelName);
            rabbitTemplate.convertAndSend(exchange, routingKey, msg);
            log.info("[Evento de Negocio] Despachado a " + channelName);
        });
        
    }
    
}
