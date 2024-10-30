package ru.artur.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.artur.project.dto.ItemDto;
import ru.artur.project.kafka.KafkaItemProducer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaProducerTestService {

    private final KafkaItemProducer kafkaItemProducer;

    @Value("${for-kafka.kafka.topic.item-creating}")
    private String topic;

    public void send(List<ItemDto> items) {
        items.forEach(dto -> kafkaItemProducer.sendTo(topic, dto));
    }
}
