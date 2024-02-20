package com.xnetcorp.notifications.channels.adapters.kafka;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Schema {
    private String type;
    private Boolean optional;
}
