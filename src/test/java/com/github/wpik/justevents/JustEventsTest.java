package com.github.wpik.justevents;

import com.github.wpik.justevents.events.InvalidEvents;
import com.github.wpik.justevents.events.NestedPayload;
import com.github.wpik.justevents.events.SomePayload;
import com.github.wpik.justevents.events.ValidEvents;
import com.github.wpik.justevents.exception.JustEventDeserializationException;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class JustEventsTest {
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
        justEvents.deserialize(ValidEvents.SOME_PAYLOAD.getJson());
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
                fail("serialization should fail for " + InvalidEvents.class.getName() + "." + e.name());
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
                fail("deserialization should fail for " + InvalidEvents.class.getName() + "." + e.name());
            } catch (Exception ex) {
                assertEquals("expected exception should be caught for " + InvalidEvents.class.getName() + "." + e.name(),
                        e.getDeserializationExpectedException(), ex.getClass());
            }
        });
    }
}
