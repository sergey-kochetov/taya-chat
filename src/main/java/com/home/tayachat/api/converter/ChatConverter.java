package com.home.tayachat.api.converter;

import com.home.tayachat.api.domain.Chat;
import com.home.tayachat.api.dto.ChatDto;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ChatConverter {
    public ChatDto toDto(Chat chat) {
        return ChatDto.builder()
                .id(chat.getId())
                .name(chat.getName())
                .createdAt(Instant.ofEpochMilli(chat.getCreatedAt()))
                .build();
    }
}
