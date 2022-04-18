package com.home.tayachat.api.dto;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private String id;
    private String name;
    private Instant createdAt;
}
