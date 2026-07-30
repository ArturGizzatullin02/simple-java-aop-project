package ru.artur.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.artur.project.dto.ItemDto;
import ru.artur.project.entity.Item;
import ru.artur.project.kafka.KafkaItemProducer;
import ru.artur.project.repository.ItemRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    @Value("${for-kafka.kafka.topic.item-creating}")
    private String topic;

    private final ItemRepository itemRepository;

    private final KafkaItemProducer kafkaItemProducer;

    public void save(List<Item> items) {
        log.info("Creating items {}", items);
        itemRepository.saveAll(items)
                .stream()
                .map(Item::getId)
                .forEach(kafkaItemProducer::send);
    }

    public void sendToCreatingTopic(List<ItemDto> items) {
        items.forEach(dto -> kafkaItemProducer.sendTo(topic, dto));
    }

    public List<ItemDto> parseJson() {
        ObjectMapper mapper = new ObjectMapper();

        ItemDto[] items;
        try {
            items = mapper.readValue(new File("src/main/resources/MOCK_DATA.json"), ItemDto[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(items);
    }
}
