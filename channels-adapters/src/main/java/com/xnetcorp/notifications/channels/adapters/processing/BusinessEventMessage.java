package com.xnetcorp.notifications.channels.adapters.processing;

import java.util.HashMap;

import lombok.Data;

@Data
public class BusinessEventMessage {
    /**
     * Corresponde con el nombre de un evento de negocio registrado en el catalogo
     * de eventos.
     */
    private String eventType;
    /**
     * Identificador del cliente (dueño) del evento del negocio. Se corresponde de todos
     * los subscriptores (clientes) registrados de a quien le pertenece la información
     * contenedida en el payload.
     */
    private String ownerId;
    /**
     * Cuerpo del mensaje del evento de negocio recibido.
     */
    HashMap<String, Object> payload;
}
