package com.github.wpik.justevents;

import com.github.wpik.justevents.events.InvalidEvents;
import com.github.wpik.justevents.events.NestedPayload;
import com.github.wpik.justevents.events.SomePayload;
import com.github.wpik.justevents.events.ValidEvents;
import com.github.wpik.justevents.exception.JustEventDeserializationException;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JustEventsTest {
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
    private JustEvents justEvents;

    @Before
    public void setup() {
        justEvents = new JustEvents();
        justEvents.registerEvent(SomePayload.NAME, SomePayload.class);
        justEvents.registerEvent(NestedPayload.NAME, NestedPayload.class);
    }

    @Test
    public void validEventSerializationTest() {
        Arrays.stream(ValidEvents.values()).forEach(e -> {
            String json = justEvents.serialize(e.getEvent());
            assertEquals(e.getJson(), json);
        });
    }

    @Test
    public void validEventDeserializationTest() {
        Arrays.stream(ValidEvents.values()).forEach(e -> {
            Event<?> event = justEvents.deserialize(e.getJson());
            assertEquals(e.getEvent(), event);
        });
    }

    @Test
    public void validEventNameDeserializationTest() {
        Arrays.stream(ValidEvents.values()).forEach(e -> {
            String name = justEvents.deserializeEventName(e.getJson());
            assertEquals(e.getEvent().getName(), name);
        });
    }

    @Test(expected = JustEventDeserializationException.class)
    public void deserializationWithoutRegisteredEventFailsTest() {
        justEvents = new JustEvents();
        Event<?> event = justEvents.deserialize(EVENT_JSON);
        assertEquals(EVENT_OBJECT, event);
    }

    @Test
    public void invalidEventNameDeserializationTest() {
        Arrays.stream(InvalidEvents.values()).forEach(e -> {
            String name = justEvents.deserializeEventName(e.getJson());
            assertEquals(e.getEvent().getName(), name);
        });
    }

    @Test
    public void invalidEventSerializationTest() {
        Arrays.stream(InvalidEvents.values()).forEach(e -> {
            try {
                justEvents.serialize(e.getEvent());
                assertTrue("serialization should fail for " + InvalidEvents.class.getName() + "." + e.name(), false);
            } catch (ConstraintViolationException ex) {
                assertTrue(true);
            }
        });
    }

    @Test
    public void invalidEventDeserializationTest() {
        Arrays.stream(InvalidEvents.values()).forEach(e -> {
            try {
                justEvents.deserialize(e.getJson());
                assertTrue("deserialization should fail for " + InvalidEvents.class.getName() + "." + e.name(), false);
            } catch (Exception ex) {
                assertEquals("expected exception should be caught for " + InvalidEvents.class.getName() + "." + e.name(),
                        e.getDeserializationExpectedException(), ex.getClass());
            }
        });
    }
}
