package com.xnetcorp.notifications.channels.adapters;

import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class RabbitMQConsumer {

    @Autowired
    private NotificationService mailService;

    @Autowired
    private NotificationService smsService;

    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * Receptor de mensajes desde cola para envíos de SMS.
     * 
     * @param message
     */
    @RabbitListener(queues = "${notification.sms.queue}")
    public void receiveMessageSms(NotificationMessage message) {
        meterRegistry.config()
			.commonTags("app", "messagingservice")
			.commonTags("component","adapters")
            .commonTags("channel", "sms")
            .commonTags("customer", Optional.ofNullable(message.getCustomerId()).orElse("-"));
        meterRegistry.counter("notificationservice.rabbitmq.messages").increment();
        this.meterRegistry.timer("notificationservice.sms.time").record(
         ()-> {
            this.smsService.send(message);
         });
    }

    /**
     * Receptor de mensajes desde cola para envíos de Correos
     * electrónicos.
     * 
     * @param message
     */
    @RabbitListener(queues = "${notification.mail.queue}")
    public void receiveMessageMail(NotificationMessage message) {
        meterRegistry.config()
			.commonTags("app", "messagingservice")
			.commonTags("component","adapters")
            .commonTags("channel", "mail")            
            .commonTags("customer", Optional.ofNullable(message.getCustomerId()).orElse("-"));
        meterRegistry.counter("notificationservice.rabbitmq.messages").increment();;
        mailService.send(message);
    }
}
