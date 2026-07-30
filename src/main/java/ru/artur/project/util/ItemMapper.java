package ru.artur.project.util;

import org.springframework.stereotype.Component;
import ru.artur.project.dto.ItemDto;
import ru.artur.project.entity.Item;

@Component
public class ItemMapper {

    public static Item toEntity(ItemDto item) {
        if (item == null) return null;
        return Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .build();
    }

    public static ItemDto toDto(Item item) {
        if (item == null) return null;
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .build();
    }
}
