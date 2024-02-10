package com.xnetcorp.notifications.channels.adapters.subscriptions.model;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data @Builder 
public class SubscriptionChannel {
    /**
     * Nombre del canal. Se emplea para definir el nombre técnico del canal
     * a través de cola en RabbitMQ.
     */
    private String chanelName;

    /**
     * Conjunto de propiedades específicas definidas por el dueño de la
     * subscription.
     */
    @Singular
    private Map<String, Object> properties; 

    @Singular
    private Set<String> ignoresProperties;
}
