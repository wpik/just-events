package com.github.wpik.justevents.exception;

public class JustEventSerializationException extends RuntimeException {
    public JustEventSerializationException(String message) {
        super(message);
    }

    public JustEventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JustEventSerializationException(Throwable cause) {
        super(cause);
    }
}
