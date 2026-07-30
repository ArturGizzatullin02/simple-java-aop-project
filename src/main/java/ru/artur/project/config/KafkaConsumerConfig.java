package ru.artur.project.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.artur.project.dto.ItemDto;
import ru.artur.project.kafka.MessageDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value("${for-kafka.kafka.consumer.group-id}")
    private String groupId;

    @Value("${for-kafka.kafka.bootstrap.server}")
    private String servers;

    @Value("${for-kafka.kafka.session.timeout-ms}")
    private String sessionTimeout;

    @Value("${for-kafka.kafka.max.max-partitions-fetch-bytes}")
    private String maxPartitionsFetchBytes;

    @Value("${for-kafka.kafka.max.max-poll-records}")
    private String maxPollRecords;

    @Value("${for-kafka.kafka.max.max-poll-interval-ms}")
    private String maxPollIntervalMs;

    @Bean
    public ConsumerFactory<String, ItemDto> consumerListenerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.artur.project.dto.ItemDto");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionsFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, MessageDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, MessageDeserializer.class);

        DefaultKafkaConsumerFactory factory = new DefaultKafkaConsumerFactory<String, ItemDto>(props);
        factory.setKeyDeserializer(new StringDeserializer());

        return factory;
    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory,
                                    ConcurrentKafkaListenerContainerFactory<String, T> containerFactory) {
        containerFactory.setConsumerFactory(consumerFactory);
        containerFactory.setBatchListener(true);
        containerFactory.setConcurrency(1);
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        containerFactory.getContainerProperties().setPollTimeout(5000);
        containerFactory.getContainerProperties().setMicrometerEnabled(true);
        containerFactory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, e, deliveryAttempt) -> {
            log.error("Retry listeners message {}, offset {}, deliveryAttempt {}", e.getMessage(), record.offset(),
                    deliveryAttempt);
        });
        return handler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ItemDto> kafkaListenerContainerFactory(@Qualifier("consumerListenerFactory") ConsumerFactory<String, ItemDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, ItemDto> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, containerFactory);
        return containerFactory;
    }
}
