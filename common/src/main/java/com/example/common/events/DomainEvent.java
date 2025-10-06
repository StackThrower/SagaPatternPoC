package com.example.common.events;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
public abstract class DomainEvent {
    private final String eventId;
    private final LocalDateTime timestamp;
    private final String sagaId;

    public DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.sagaId = null;
    }

    public DomainEvent(String sagaId) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.sagaId = sagaId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSagaId() {
        return sagaId;
    }
}
