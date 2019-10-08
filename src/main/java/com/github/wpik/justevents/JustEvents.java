package com.github.wpik.justevents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.wpik.justevents.exception.JustEventDeserializationException;
import com.github.wpik.justevents.exception.JustEventSerializationException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Set;

public class JustEvents {
    private static final TypeReference<Event<?>> EVENT_TYPE_REFERENCE = new TypeReference<Event<?>>() {
    };

    private ObjectMapper eventObjectMapper = createEventObjectMapper();
    private ObjectMapper metadataObjectMapper = createMetadataObjectMapper();

    private Validator validator = createValidator();

    private ObjectMapper createEventObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setDateFormat(SimpleDateFormat.getDateTimeInstance());
        return objectMapper;
    }

    private static ObjectMapper createMetadataObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        return objectMapper;
    }

    private static Validator createValidator() {
        return Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();
    }

    public <P extends Payload> void registerEvent(String eventName, Class<P> payloadClass) {
        this.eventObjectMapper.registerSubtypes(new NamedType(payloadClass, eventName));
    }

    public String serialize(Event<?> event) {
        try {
            validate(event);
            return eventObjectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new JustEventSerializationException(e);
        }
    }

    public String deserializeEventName(String json) {
        try {
            return metadataObjectMapper.readValue(json, Metadata.class).getName();
        } catch (IOException e) {
            throw new JustEventDeserializationException(e);
        }
    }

    public <P extends Payload> Event<P> deserialize(String json) {
        try {
            Event<P> event = eventObjectMapper.readValue(json, EVENT_TYPE_REFERENCE);
            validate(event);
            return event;
        } catch (IOException e) {
            throw new JustEventDeserializationException(e);
        }
    }

    private <T> void validate(T event) {
        Set<ConstraintViolation<T>> violations = validator.validate(event);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
