package com.xnetcorp.notifications.channels.adapters;

public interface NotificationService {
    /**
     * Realiza el envío de la notificacion a través del canal
     * específico según la implementación de esta interface.
     * 
     * @param message
     */
    void send(NotificationMessage message);
}
