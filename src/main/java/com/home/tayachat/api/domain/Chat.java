package com.home.tayachat.api.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat implements Serializable {

    @Builder.Default
    private String id = UUID.randomUUID().toString().substring(0, 4);

    private String name;

    @Builder.Default
    private Long createdAt = Instant.now().toEpochMilli();
}