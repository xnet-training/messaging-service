package com.xnetcorp.notifications.channels.adapters.kafka;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class BusinessEventMessage {
    private Schema schema;
    private String payload;
}
