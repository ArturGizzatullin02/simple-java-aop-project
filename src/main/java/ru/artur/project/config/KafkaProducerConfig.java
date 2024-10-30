package ru.artur.project.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.artur.project.dto.ItemDto;
import ru.artur.project.kafka.KafkaItemProducer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${for-kafka.kafka.bootstrap.server}")
    private String servers;

    @Value("${for-kafka.kafka.topic.item-created}")
    private String itemTopic;

    @Bean("item")
    public KafkaTemplate<String, ItemDto> kafkaTemplate(ProducerFactory<String, ItemDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    @ConditionalOnProperty(
            value = "for-kafka.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true
    )
    public KafkaItemProducer producerItem(@Qualifier("item") KafkaTemplate<String, ItemDto> kafkaTemplate) {
        kafkaTemplate.setDefaultTopic(itemTopic);
        return new KafkaItemProducer(kafkaTemplate);
    }

    @Bean()
    public ProducerFactory<String, ItemDto> producerItemFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }
}
