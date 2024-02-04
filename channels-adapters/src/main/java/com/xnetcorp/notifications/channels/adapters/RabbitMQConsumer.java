package com.xnetcorp.notifications.channels.adapters;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @Autowired
    private NotificationService mailService;

    @Autowired
    private NotificationService smsService;

    /**
     * Receptor de mensajes desde cola para envíos de SMS.
     * 
     * @param message
     */
    @RabbitListener(queues = "${notification.sms.queue}")
    public void receiveMessageSms(NotificationMessage message) {
        this.smsService.send(message);
    }

    /**
     * Receptor de mensajes desde cola para envíos de Correos
     * electrónicos.
     * 
     * @param message
     */
    @RabbitListener(queues = "${notification.mail.queue}")
    public void receiveMessageMail(NotificationMessage message) {
        mailService.send(message);
    }
}
