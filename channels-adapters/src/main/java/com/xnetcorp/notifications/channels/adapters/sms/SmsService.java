package com.xnetcorp.notifications.channels.adapters.sms;

import org.springframework.stereotype.Service;
import com.xnetcorp.notifications.channels.adapters.NotificationMessage;
import com.xnetcorp.notifications.channels.adapters.NotificationService;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase implementa el servicio para el envío de mensajes a través
 * de servicio SMS.
 */
@Slf4j
@Service
public class SmsService implements NotificationService {

    @Override
    public void send(NotificationMessage message) {
        log.info(message.toString());
        log.info(String.format("Enviando mensaje SMS '%s' al telefono %s", 
            message.getMessage(), 
            message.getProperties().get("number"))
        );
    }
}
