package com.github.wpik.justevents.exception;

public class JustEventDeserializationException extends RuntimeException {
    public JustEventDeserializationException(String message) {
        super(message);
    }

    public JustEventDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustEventDeserializationException(Throwable cause) {
        super(cause);
    }
}
