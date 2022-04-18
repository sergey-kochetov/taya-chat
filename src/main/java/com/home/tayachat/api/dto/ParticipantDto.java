package com.home.tayachat.api.dto;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {

    private String id;

    private Instant enterAt;
}