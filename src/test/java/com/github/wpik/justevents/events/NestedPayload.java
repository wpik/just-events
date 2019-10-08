package com.github.wpik.justevents.events;

import com.github.wpik.justevents.Payload;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@Builder
public class NestedPayload implements Payload {

    public static final String NAME = "nested.event";

    @Valid
    @NotNull
    private Person person;

    @Valid
    @NotNull
    private Address address;

    @Override
    public String getName() {
        return NAME;
    }

    @Data
    @Builder
    public static class Person {
        @NotBlank
        private String name;
        @Positive
        private int age;
    }

    @Data
    @Builder
    public static class Address {
        @NotBlank
        private String street;
        @NotBlank
        private String city;
        @Pattern(regexp = "^\\d\\d-\\d\\d\\d$")
        private String zipCode;
    }
}
