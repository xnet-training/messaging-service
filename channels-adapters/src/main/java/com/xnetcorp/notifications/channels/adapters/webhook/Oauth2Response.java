package com.xnetcorp.notifications.channels.adapters.webhook;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class Oauth2Response {
    private String access_token;
    private Integer expires_in;
    private Integer refresh_expires_in;
    private String refresh_token;
    private String session_state;
}
