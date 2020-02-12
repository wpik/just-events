package com.github.wpik.justevents.events;

import com.github.wpik.justevents.Event;
import com.github.wpik.justevents.exception.JustEventDeserializationException;

import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public enum InvalidEvents {
    SOME_PAYLOAD_WRONG_FOO(Event.builder()
            .id("686f4edd-e8d6-4f23-a053-046b2ddf60ed")
            .timestamp(OffsetDateTime.parse("2019-01-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(SomePayload.builder()
                    .foo("")
                    .build())
            .build(),
            "{\"id\":\"686f4edd-e8d6-4f23-a053-046b2ddf60ed\",\"timestamp\":\"2019-01-01T12:00:00Z\",\"name\":\"some.event\",\"payload\":{\"foo\":\"\"}}",
            ConstraintViolationException.class),

    SOME_PAYLOAD_MISSING_ID(Event.builder()
            .timestamp(OffsetDateTime.parse("2019-01-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(SomePayload.builder()
                    .build())
            .build(),
            "{\"timestamp\":\"2019-01-01T12:00:00Z\",\"name\":\"some.event\",\"payload\":{\"foo\":\"bar\"}}",
            JustEventDeserializationException.class),

    SOME_PAYLOAD_MISSING_TIMESTAMP(Event.builder()
            .id("686f4edd-e8d6-4f23-a053-046b2ddf60ed")
            .payload(SomePayload.builder()
                    .build())
            .build(),
            "{\"id\":\"686f4edd-e8d6-4f23-a053-046b2ddf60ed\",\"name\":\"some.event\",\"payload\":{\"foo\":\"bar\"}}",
            JustEventDeserializationException.class),

    SOME_PAYLOAD_WRONG_TIMESTAMP(Event.builder()
            .id("686f4edd-e8d6-4f23-a053-046b2ddf60ed")
            .timestamp(OffsetDateTime.parse("2019-01-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(SomePayload.builder()
                    .build())
            .build(),
            "{\"id\":\"686f4edd-e8d6-4f23-a053-046b2ddf60ed\",\"timestamp\":\"WRONG TIMESTAMP\",\"name\":\"some.event\",\"payload\":{\"foo\":\"bar\"}}",
            JustEventDeserializationException.class),

    NESTED_PAYLOAD_MISSING_ADDRESS(Event.builder()
            .id("bb03940c-b8c2-4a86-9ecb-d6282542f22d")
            .timestamp(OffsetDateTime.parse("2019-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(NestedPayload.builder()
                    .person(NestedPayload.Person.builder()
                            .name("Jan Kowalski")
                            .age(42)
                            .build())
                    .build())
            .build(),
            "{\"id\":\"bb03940c-b8c2-4a86-9ecb-d6282542f22d\",\"timestamp\":\"2019-01-01T00:00:00Z\",\"name\":\"nested.event\",\"payload\":{\"person\":{\"name\":\"Jan Kowalski\",\"age\":42},\"address\":{}}}",
            JustEventDeserializationException.class),

    NESTED_PAYLOAD_WRONG_ADDRESS(Event.builder()
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
                            .zipCode("wrong value")
                            .build())
                    .build())
            .build(),
            "{\"id\":\"bb03940c-b8c2-4a86-9ecb-d6282542f22d\",\"timestamp\":\"2019-01-01T00:00:00Z\",\"name\":\"nested.event\",\"payload\":{\"person\":{\"name\":\"Jan Kowalski\",\"age\":42},\"address\":{\"street\":\"Al. Jerozolimskie 111\",\"city\":\"Warszawa\",\"zipCode\":\"wrong value\"}}}",
            ConstraintViolationException.class);

    private Event<?> event;
    private String json;
    private Class<? extends Exception> deserializationExpectedException;

    InvalidEvents(Event<?> event, String json, Class<? extends Exception> deserializationExpectedException) {
        this.event = event;
        this.json = json;
        this.deserializationExpectedException = deserializationExpectedException;
    }

    public Event<?> getEvent() {
        return event;
    }

    public String getJson() {
        return json;
    }

    public Class<? extends Exception> getDeserializationExpectedException() {
        return deserializationExpectedException;
    }
}
