package com.xnetcorp.notifications.channels.adapters.processing;

public interface BusinessEventProcessingService {
    /**
     * Permite procesar todos los eventos de negocio que son notificados a través
     * del canal de recepción de eventos de negocio.
     * 
     * @param message
     */
    public void execute(BusinessEventMessage message);
}
