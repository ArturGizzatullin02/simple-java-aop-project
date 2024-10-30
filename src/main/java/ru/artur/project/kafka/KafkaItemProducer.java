package ru.artur.project.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaItemProducer {

    private final KafkaTemplate kafkaTemplate;

    public void send(Long id) {
        try {
            kafkaTemplate.sendDefault(UUID.randomUUID().toString(), id).get();
            kafkaTemplate.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendTo(String topic, Object o) {
        try {
            kafkaTemplate.send(topic, UUID.randomUUID().toString(), o).get();
            kafkaTemplate.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
