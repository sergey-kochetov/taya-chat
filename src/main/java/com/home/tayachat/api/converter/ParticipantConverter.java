package com.home.tayachat.api.converter;

import com.home.tayachat.api.domain.Participant;
import com.home.tayachat.api.dto.ParticipantDto;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ParticipantConverter {
    public ParticipantDto toDto(Participant participant) {
        return ParticipantDto.builder()
                .id(participant.getId())
                .enterAt(Instant.ofEpochMilli(participant.getEnterAt()))
                .build();
    }
}
