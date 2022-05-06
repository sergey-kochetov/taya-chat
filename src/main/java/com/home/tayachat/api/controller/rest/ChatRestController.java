package com.home.tayachat.api.controller.rest;

import com.home.tayachat.api.converter.ChatConverter;
import com.home.tayachat.api.dto.ChatDto;
import com.home.tayachat.api.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@RestController
public class ChatRestController {
    private ChatService chatService;
    private ChatConverter chatConverter;

    public static final String FETCH_CHATS = "/api/chats";

    @GetMapping(value = FETCH_CHATS, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ChatDto> fetchChats() {
        return chatService
                .getChats()
                .map(chatConverter::toDto)
                .collect(Collectors.toList());
    }
}
