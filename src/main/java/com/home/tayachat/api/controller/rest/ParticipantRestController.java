package com.home.tayachat.api.controller.rest;

import com.home.tayachat.api.converter.ParticipantConverter;
import com.home.tayachat.api.dto.ParticipantDto;
import com.home.tayachat.api.service.ParticipantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@RestController
public class ParticipantRestController {
    private final ParticipantService participantService;
    private final ParticipantConverter participantConverter;

    public static final String FETCH_PARTICIPANTS = "/api/chats/{chat_id}/participants";

    @GetMapping(value = FETCH_PARTICIPANTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParticipantDto> fetchParticipants(@PathVariable("chat_id") String chatId) {
        return participantService
                .getParticipants(chatId)
                .map(participantConverter::toDto)
                .collect(Collectors.toList());
    }
}
