package com.github.wpik.justevents;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Payload {
    @JsonIgnore
    String getName();
}
