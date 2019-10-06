package com.github.wpik.justevents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.wpik.justevents.exception.JustEventDeserializationException;
import com.github.wpik.justevents.exception.JustEventSerializationException;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class JustEvents {
    private static final TypeReference<Event<?>> EVENT_TYPE_REFERENCE = new TypeReference<Event<?>>() {
    };

    private ObjectMapper objectMapper = createObjectMapper();

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setDateFormat(SimpleDateFormat.getDateTimeInstance());
        return objectMapper;
    }

    public String serialize(Event<?> event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new JustEventSerializationException(e);
        }
    }

    public <P extends Payload> Event<P> deserialize(String json) {
        try {
            return objectMapper.readValue(json, EVENT_TYPE_REFERENCE);
        } catch (IOException e) {
            throw new JustEventDeserializationException(e);
        }
    }
}
