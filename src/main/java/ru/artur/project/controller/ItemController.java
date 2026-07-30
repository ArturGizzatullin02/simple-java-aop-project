package ru.artur.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.artur.project.dto.ItemDto;
import ru.artur.project.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/parse")
    public void parseSource() {
        List<ItemDto> itemDtos = itemService.parseJson();
        itemService.sendToCreatingTopic(itemDtos);
    }
}
