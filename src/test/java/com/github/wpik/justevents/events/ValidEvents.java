package com.github.wpik.justevents.events;

import com.github.wpik.justevents.Event;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public enum ValidEvents {
    SOME_PAYLOAD(Event.builder()
            .id("686f4edd-e8d6-4f23-a053-046b2ddf60ed")
            .timestamp(OffsetDateTime.parse("2019-01-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(SomePayload.builder()
                    .foo("bar")
                    .build())
            .build(),
            "{\"id\":\"686f4edd-e8d6-4f23-a053-046b2ddf60ed\",\"timestamp\":\"2019-01-01T12:00:00Z\",\"name\":\"some.event\",\"payload\":{\"foo\":\"bar\"}}"),

    NESTED_PAYLOAD(Event.builder()
            .id("bb03940c-b8c2-4a86-9ecb-d6282542f22d")
            .timestamp(OffsetDateTime.parse("2019-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(NestedPayload.builder()
                    .person(NestedPayload.Person.builder()
                            .name("Jan Kowalski")
                            .age(42)
                            .build())
                    .address(NestedPayload.Address.builder()
                            .city("Warszawa")
                            .street("Al. Jerozolimskie 111")
                            .zipCode("01-011")
                            .build())
                    .build())
            .build(),
            "{\"id\":\"bb03940c-b8c2-4a86-9ecb-d6282542f22d\",\"timestamp\":\"2019-01-01T00:00:00Z\",\"name\":\"nested.event\",\"payload\":{\"person\":{\"name\":\"Jan Kowalski\",\"age\":42},\"address\":{\"street\":\"Al. Jerozolimskie 111\",\"city\":\"Warszawa\",\"zipCode\":\"01-011\"}}}");

    private Event<?> event;
    private String json;

    ValidEvents(Event<?> event, String json) {
        this.event = event;
        this.json = json;
    }

    public Event<?> getEvent() {
        return event;
    }

    public String getJson() {
        return json;
    }
}
