package com.github.wpik.justevents;

import com.github.wpik.justevents.events.InvalidEvents;
import com.github.wpik.justevents.events.NestedPayload;
import com.github.wpik.justevents.events.SomePayload;
import com.github.wpik.justevents.events.ValidEvents;
import com.github.wpik.justevents.exception.JustEventDeserializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JustEventsTest {
    private JustEvents justEvents;

    @BeforeEach
    void setup() {
        justEvents = new JustEvents();
        justEvents.registerEvent(SomePayload.NAME, SomePayload.class);
        justEvents.registerEvent(NestedPayload.NAME, NestedPayload.class);
    }

    @ParameterizedTest
    @EnumSource(ValidEvents.class)
    void validEventSerializationTest(ValidEvents validEvent) {
        String json = justEvents.serialize(validEvent.getEvent());
        assertEquals(validEvent.getJson(), json);
    }

    @ParameterizedTest
    @EnumSource(ValidEvents.class)
    void validEventDeserializationTest(ValidEvents validEvent) {
        Event<?> event = justEvents.deserialize(validEvent.getJson());
        assertEquals(validEvent.getEvent(), event);
    }

    @ParameterizedTest
    @EnumSource(ValidEvents.class)
    void validEventNameDeserializationTest(ValidEvents validEvent) {
        String name = justEvents.deserializeEventName(validEvent.getJson());
        assertEquals(validEvent.getEvent().getName(), name);
    }

    @Test
    void deserializationWithoutRegisteredEventFailsTest() {
        assertThrows(JustEventDeserializationException.class, () -> {
            justEvents = new JustEvents();
            justEvents.deserialize(ValidEvents.SOME_PAYLOAD.getJson());
        });
    }

    @ParameterizedTest
    @EnumSource(InvalidEvents.class)
    void invalidEventNameDeserializationTest(InvalidEvents invalidEvent) {
        String name = justEvents.deserializeEventName(invalidEvent.getJson());
        assertEquals(invalidEvent.getEvent().getName(), name);
    }

    @ParameterizedTest
    @EnumSource(InvalidEvents.class)
    void invalidEventSerializationTest(InvalidEvents invalidEvent) {
        assertThrows(ConstraintViolationException.class, () -> justEvents.serialize(invalidEvent.getEvent()));
    }

    @ParameterizedTest
    @EnumSource(InvalidEvents.class)
    void invalidEventDeserializationTest(InvalidEvents invalidEvent) {
        Throwable throwable = assertThrows(RuntimeException.class, () -> justEvents.deserialize(invalidEvent.getJson()));

        assertEquals(invalidEvent.getDeserializationExpectedException(), throwable.getClass());
    }
}
