# just-events

Library for easy handling of JSON events in microservices.

## Event structure

Each event has the following structure:

```json
{
  "id": "0b3b998d-65da-41a2-9f2a-f8ea7187d571",
  "timestamp": "2019-01-01T00:00:00Z",
  "name": "some.event",
  "payload": {
    "foo": "bar"
  }
}
```

## Usage

### Payload class

Event is represented by `Event<P extends Payload>` class, where `P` is the event payload type defined by the library 
user.

You need to define some POJO that implements the `Payload` interface, e.g.:

```java
package com.github.wpik.justevents.events;

import com.github.wpik.justevents.Payload;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class SomePayload implements Payload {
    public static final String NAME = "some.event";

    @NotBlank
    private String foo;

    @Override
    public String getName() {
        return NAME;
    }
}
```

### Bootstrap the library

Then you create the instance of `JustEvents` class and register the payload with it. After this, you can use that 
instance to serialize and deserialize events.

```
JustEvents justEvents=new JustEvents();
justEvents.registerEvent(SomePayload.NAME, SomePayload.class);
```

## Serialization

To serialize event, create it and use `JustEvents.serialize()` merhod:

```
Event<?> event = Event.builder()
    .id("686f4edd-e8d6-4f23-a053-046b2ddf60ed")
    .timestamp(OffsetDateTime.parse("2019-01-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    .payload(SomePayload.builder()
        .foo("bar")
        .build())
    .build();

String json = justEvents.serialize(event);
```

`json` will have this value:

```json
{
   "id":"686f4edd-e8d6-4f23-a053-046b2ddf60ed",
   "timestamp":"2019-01-01T12:00:00Z",
   "name":"some.event",
   "payload":{
      "foo":"bar"
   }
}
```

## Deserialization

To deserialize use `JustEvents.deserializeEventName()` and then `JustEvents.deserialize()` methods. First method will
just deserialize the event name. This is needed if your service may receive some other events that you don't want to 
process in the service. This way you can check the event name and deserialize it only if you are interested in it.

```
String json = ...
String eventName = justEvents.deserializeEventName(json);

if("some.event".equals(eventName)){
    Event<SomePayload> event = justEvents.deserialize(json);
    // process event
}
```
