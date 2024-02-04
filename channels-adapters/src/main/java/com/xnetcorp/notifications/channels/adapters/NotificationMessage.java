package com.xnetcorp.notifications.channels.adapters;

import java.util.HashMap;

import lombok.Data;

@Data
public class NotificationMessage {
    /**
     * Identificador único del mensaje
     */
    private String id;
    /**
     * Nombre del canal de comunicación
     */
    private String channel;
    /**
     * Texto del mensaje a transmitir. Puede ser texto enriquecido
     * por ejemplo HTML.
     */
    private String message;
    /**
     * Propiedades específicas de acuredo con el canal.
     */
    private HashMap<String, Object> properties;
}

