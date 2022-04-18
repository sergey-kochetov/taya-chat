package com.home.tayachat.api.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participant implements Serializable {

    @Builder.Default
    private Long enterAt = Instant.now().toEpochMilli();

    private String id;

    private String sessionId;

    private String chatId;
}
