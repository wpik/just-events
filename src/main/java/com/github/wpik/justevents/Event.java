package com.github.wpik.justevents;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "timestamp", "name", "payload"})
public class Event<P extends Payload> {
    @Builder.Default
    @NotBlank
    private String id = UUID.randomUUID().toString();

    @Builder.Default
    @NotNull
    private OffsetDateTime timestamp = OffsetDateTime.now();

    @ToString.Include(name = "name")
    @NotBlank
    public String getName() {
        return payload.getName();
    }

    @Valid
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, visible = true, property = "name")
    private P payload;
}

