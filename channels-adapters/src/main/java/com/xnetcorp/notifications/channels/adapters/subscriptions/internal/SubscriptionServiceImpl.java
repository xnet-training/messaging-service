package com.xnetcorp.notifications.channels.adapters.subscriptions.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.xnetcorp.notifications.channels.adapters.subscriptions.SubscriptionService;
import com.xnetcorp.notifications.channels.adapters.subscriptions.model.SubscriptionChannel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public List<SubscriptionChannel> findSubscriptionsChannels(String ownerId, String eventType) {
        List<SubscriptionChannel> results = new ArrayList<>();

        if(eventType.equalsIgnoreCase("CreditoAprobado")) {
            results.add(SubscriptionChannel.builder()
                .chanelName("sms")
                .properties(Map.of(
                    "number", "992767644", 
                    "countryCode", 51,
                    "message", "Credito aprobado monto: $message.payload.moneda $message.payload.monto, plazo: $message.payload.plazo, cuotas de: $message.payload.moneda $message.payload.cuotas"))
                    .ignoresProperties(Set.of("message"))
                .build());

            results.add(SubscriptionChannel.builder()
                .chanelName("mail")
                .properties(Map.of(
                    "mail", "ilver.anache@gmail.com",
                    "subject", "[MAIL] Credito Aprobado por $message.payload.moneda $message.payload.monto",
                    "message", "<html><body><b>Credito por</b> $message.payload.moneda $message.payload.monto aprobado a $message.payload.plazo meses con cuotas de $message.payload.moneda $message.payload.cuotas</body></html>"))
                .ignoresProperties(Set.of("message", "subject"))
                .build());
        } else {
            results.add(SubscriptionChannel.builder()
                .chanelName("sms")
                .properties(Map.of(
                    "number", "992767644", 
                    "countryCode", 51,
                    "message", "Cuota vencida $message.payload.cuota, monto: $message.payload.moneda $message.payload.montoCuota"))
                    .ignoresProperties(Set.of("message"))
                .build());

            results.add(SubscriptionChannel.builder()
                .chanelName("mail")
                .properties(Map.of(
                    "mail", "ilver.anache@gmail.com",
                    "subject", "[MAIL] Cuota Vencida $message.payload.cuota",
                    "message", "<html><body><h1>Aviso de Cuota Vencida</h1><p><b>Cuota vencida</b> $message.payload.cuota, <b>monto</b>: $message.payload.moneda $message.payload.montoCuota</p></body></html>"))
                .ignoresProperties(Set.of("message", "subject"))
                .build());
        }
        return results;
    }
    
}
