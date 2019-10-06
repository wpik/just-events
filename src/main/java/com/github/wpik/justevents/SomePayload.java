package com.github.wpik.justevents;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SomePayload implements Payload{
    private String foo;

    @Override
    public String getName() {
        return "some.event";
    }
}
