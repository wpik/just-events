package com.github.wpik.justevents;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

    private ObjectMapper objectMapper = createObjectMapper();
    private Validator validator = createValidator();

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setDateFormat(SimpleDateFormat.getDateTimeInstance());
        return objectMapper;
    }

    private static Validator createValidator() {
        return Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();
    }

    public String serialize(Event<?> event) {
        try {
            validate(event);
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new JustEventSerializationException(e);
        }
    }

    public <P extends Payload> Event<P> deserialize(String json) {
        try {
            Event<P> event = objectMapper.readValue(json, EVENT_TYPE_REFERENCE);
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
