package com.github.wpik.justevents;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SomePayload implements Payload{
    @NotBlank
    private String foo;

    @Override
    public String getName() {
        return "some.event";
    }
}
