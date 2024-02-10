package com.xnetcorp.notifications.channels.adapters.webhook;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xnetcorp.notifications.channels.adapters.NotificationMessage;
import com.xnetcorp.notifications.channels.adapters.NotificationService;

import jakarta.mail.Multipart;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebhookService  implements NotificationService {

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void send(NotificationMessage message) {
        String ep = (String)message.getProperties().get("url");

        @SuppressWarnings("unchecked")
        HashMap<String, Object> security = (HashMap<String, Object>)message.getProperties().get("security");

        String epAuth = (String)security.get("url");
        String clientId = (String)security.get("clientId");
        String clientSecret = (String)security.get("clientSecret");
        String credentialType = (String)security.get("grantType");
        String username = (String)security.get("username");
        String password = (String)security.get("password");

        String tokenJwt = getTokenJwt( epAuth, clientId, clientSecret, credentialType, username, password );

        log.info(message.toString());
        log.info(String.format("Enviando mensaje Webhook '%s' con token '%s'", ep, tokenJwt));

        sendRequest( ep, tokenJwt, message );

    }

    private void sendRequest(String ep, String tokenJwt, NotificationMessage message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth( tokenJwt );

        this.restTemplate.postForEntity(ep,
            headers, 
            String.class,
            message.getMessage()
        );
    }

    private String getTokenJwt(String authUrl, String clientId, String clientSecret, String credentialType, String username, String password ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", credentialType);
        requestBody.add("username", username);
        requestBody.add("password", password);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Oauth2Response> response = this.restTemplate.postForEntity(authUrl, request, Oauth2Response.class);
        String token = response.getBody().getAccess_token();
        //ResponseEntity<String> response = this.restTemplate.postForEntity(authUrl, request, String.class);
        log.info(response.getBody().toString());
        log.info(String.format("HTTP Status Code %d", response.getStatusCode().value()));

        log.info(String.format("Token JWT: %s", token));
        return token;
    }
    
}
