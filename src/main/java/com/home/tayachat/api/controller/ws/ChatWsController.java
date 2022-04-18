package com.home.tayachat.api.controller.ws;

import com.home.tayachat.api.domain.Chat;
import com.home.tayachat.api.dto.ChatDto;
import com.home.tayachat.api.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@AllArgsConstructor
public class ChatWsController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public static final String CREATE_CHAT = "/topic/chats.create";

    public static final String FETCH_CREATE_CHAT_EVENT = "/topic/chats.create.event";
    public static final String FETCH_DELETE_CHAT_EVENT = "/topic/chats.delete.event";

    public static final String SEND_MESSAGE_TO_PARTICIPANT = "/topic/chats.{chat_id}.participants.{participant_id}.messages.send";

    public static final String FETCH_MESSAGES = "/topic/chats.{chat_id}.messages";
    public static final String FETCH_PERSONAL_MESSAGES = "/topic/chats.{chat_id}.participants.{participant_id}";

    @MessageMapping(CREATE_CHAT)
    public void createChat(@DestinationVariable("") String chatName) {
        Chat chat = Chat.builder()
                .name(chatName)
                .build();
        messagingTemplate.convertAndSend(FETCH_CREATE_CHAT_EVENT,
                ChatDto.builder()
                        .name(chatName)
                        .createdAt(Instant.now())
                        .build()
        );
    }

    @SubscribeMapping(FETCH_CREATE_CHAT_EVENT)
    public ChatDto fetchCreateChatEvent() {
        return null;
    }
}
