package com.github.wpik.justevents;

import com.github.wpik.justevents.exception.JustEventDeserializationException;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class JustEventsTest {
    private JustEvents justEvents = new JustEvents();

    private final static String EVENT_JSON = "{\"id\":\"686f4edd-e8d6-4f23-a053-046b2ddf60ed\",\"timestamp\":\"2019-01-01T12:00:00Z\",\"name\":\"some.event\",\"payload\":{\"foo\":\"bar\"}}";
    private final static Event<?> EVENT_OBJECT = Event.builder()
            .id("686f4edd-e8d6-4f23-a053-046b2ddf60ed")
            .timestamp(OffsetDateTime.parse("2019-01-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(SomePayload.builder()
                    .foo("bar")
                    .build())
            .build();

    private final static String INVALID_EVENT_JSON = "{\"id\":\"686f4edd-e8d6-4f23-a053-046b2ddf60ed\",\"timestamp\":\"2019-01-01T12:00:00Z\",\"name\":\"some.event\",\"payload\":{\"foo\":\"\"}}";
    private final static Event<?> INVALID_EVENT_OBJECT = Event.builder()
            .id("686f4edd-e8d6-4f23-a053-046b2ddf60ed")
            .timestamp(OffsetDateTime.parse("2019-01-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            .payload(SomePayload.builder()
                    .foo("")
                    .build())
            .build();

    @Test
    public void serializationTest() {
        String json = justEvents.serialize(EVENT_OBJECT);
        assertEquals(EVENT_JSON, json);
    }

    @Test
    public void deserializationTest() {
        justEvents.registerEvent("some.event", SomePayload.class);
        Event<?> event = justEvents.deserialize(EVENT_JSON);
        assertEquals(EVENT_OBJECT, event);
    }

    @Test(expected = JustEventDeserializationException.class)
    public void deserializationWithoutRegisteredEventFailsTest() {
        Event<?> event = justEvents.deserialize(EVENT_JSON);
        assertEquals(EVENT_OBJECT, event);
    }

    @Test
    public void nameDeserializationTest() {
        String name = justEvents.deserializeEventName(EVENT_JSON);
        assertEquals("some.event", name);
    }

    @Test
    public void invalidEventNameDeserializationTest() {
        String name = justEvents.deserializeEventName(INVALID_EVENT_JSON);
        assertEquals("some.event", name);
    }

    @Test(expected = ConstraintViolationException.class)
    public void invalidEventSerializationTest() {
        String json = justEvents.serialize(INVALID_EVENT_OBJECT);
        assertEquals(EVENT_JSON, json);
    }

    @Test(expected = ConstraintViolationException.class)
    public void invalidEventDeserializationTest() {
        justEvents.registerEvent("some.event", SomePayload.class);
        Event<?> event = justEvents.deserialize(INVALID_EVENT_JSON);
        assertEquals(EVENT_OBJECT, event);
    }
}
