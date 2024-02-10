package com.xnetcorp.notifications.channels.adapters.sms;

import org.springframework.stereotype.Service;
import com.xnetcorp.notifications.channels.adapters.NotificationMessage;
import com.xnetcorp.notifications.channels.adapters.NotificationService;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase implementa el servicio para el envío de mensajes a través
 * de servicio SMS.
 */
@Slf4j
@Service
public class SmsService implements NotificationService {

    @Timed
    @Override
    public void send(NotificationMessage message) {
        log.info(message.toString());
        log.info(String.format("[SMS] Enviando mensaje SMS '%s' al telefono %s", 
            message.getMessage(), 
            message.getProperties().get("number"))
        );
    }
}
