package ru.artur.project.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.artur.project.dto.ItemDto;
import ru.artur.project.entity.Item;
import ru.artur.project.service.ItemService;
import ru.artur.project.util.ItemMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaItemConsumer {

    private final ItemService itemService;

    @KafkaListener(
            id = "demo-group",
            topics = "item-creating",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listener(@Payload List<ItemDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Item consumer: Starting reading new messages");
        try {
            List<Item> items = messageList.stream()
                    .map(dto -> {
                        dto.setName(key + "@" + dto.getName());
                        return ItemMapper.toEntity(dto);
                    })
                    .toList();
            itemService.save(items);
        } finally {
            ack.acknowledge();
        }
        log.debug("All records have been processed");
    }
}
