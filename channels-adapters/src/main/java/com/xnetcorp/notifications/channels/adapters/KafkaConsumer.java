package com.xnetcorp.notifications.channels.adapters;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xnetcorp.notifications.channels.adapters.processing.BusinessEventMessage;
import com.xnetcorp.notifications.channels.adapters.processing.BusinessEventProcessingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableKafka
public class KafkaConsumer {

    @Autowired
    private BusinessEventProcessingService processingService;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${notification.kafka.topic}")
    private String cdcTopic;

    @Value("${notification.kafka.partitions}")
    private Integer partitions;

    @Value("${notification.kafka.replication-factor}")
    private Integer replicationFactor;

    @Value("${notification.kafka.deadletter}")
    private String deadletter;

    @KafkaListener(id="cdc-group", topics = "${notification.kafka.topic}")
    public void processMessage(ConsumerRecord record) {
        log.info(String.format("Recibiendo mensaje topico %s y mensaje '%s'",
            //"Recibiendo mensaje topico %s con epoc %d y mensaje '%s'" , 
            record.topic(),/*
            record.leaderEpoch(),*/
            record.toString()
        ));
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = 
                mapper.readValue(record.value().toString(), 
                    new TypeReference<Map<String, Object>>(){});

            String payload = (String)map.get("payload");
            log.info(String.format("Recibiendo mensaje '%s'", payload));

            /*processingService.execute(BusinessEventMessage.builder()
                .eventType(record.topic())
                .payload( mapper.readValue(payload, new TypeReference<HashMap<String, Object>>(){}) )
                .build());*/
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
	public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
		return new DefaultErrorHandler(
				new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
	}

    @Bean
	public RecordMessageConverter converter() {
		return new JsonMessageConverter();
	}

    @Bean
	public NewTopic topic() {
		return new NewTopic(cdcTopic, partitions, replicationFactor.shortValue());
	}

	@Bean
	public NewTopic dlt() {
		return new NewTopic(deadletter, 1, (short)1);
	}

}
