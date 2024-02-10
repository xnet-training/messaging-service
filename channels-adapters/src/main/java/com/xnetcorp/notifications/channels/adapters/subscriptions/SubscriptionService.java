package com.xnetcorp.notifications.channels.adapters.subscriptions;

import java.util.List;

import com.xnetcorp.notifications.channels.adapters.subscriptions.model.SubscriptionChannel;

public interface SubscriptionService {
    /**
     * Obtiene todos los canales requeridos para la subscripcion de los eventos
     * a los que se les debe notificar al dueño de negocio.
     */
    public List<SubscriptionChannel> findSubscriptionsChannels(String ownerId, String eventType);
    
}
