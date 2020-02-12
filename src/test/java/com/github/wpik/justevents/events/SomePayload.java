package com.github.wpik.justevents.events;

import com.github.wpik.justevents.Payload;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SomePayload implements Payload {
    public static final String NAME = "some.event";

    @NotBlank
    private String foo;

    @Override
    public String getName() {
        return NAME;
    }
}
