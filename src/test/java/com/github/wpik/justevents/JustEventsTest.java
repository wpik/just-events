package com.github.wpik.justevents;

import org.junit.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class JustEventsTest {
    private JustEvents justEvents = new JustEvents();

    private final static String EVENT_JSON = "{\"id\":\"686f4edd-e8d6-4f23-a053-046b2ddf60ed\",\"timestamp\":\"2019-01-01T12:00:00Z\",\"name\":\"some.event\",\"payload\":{\"foo\":\"bar\",\"name\":\"some.event\"}}";
    private final static Event<?> EVENT_OBJECT = Event.builder()
            .id("686f4edd-e8d6-4f23-a053-046b2ddf60ed")
            .timestamp(OffsetDateTime.parse("2019-01-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(SomePayload.builder()
                    .foo("bar")
                    .build())
            .build();

    @Test
    public void serializationTest() {
        String json = justEvents.serialize(EVENT_OBJECT);
        assertEquals(EVENT_JSON, json);
    }

    @Test
    public void deserializationTest() {
        Event<?> event = justEvents.deserialize(EVENT_JSON);
        assertEquals(EVENT_OBJECT, event);
    }
}
