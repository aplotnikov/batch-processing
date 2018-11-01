package io.github.aplotnikov.batch.processing.reactor.entities;

import lombok.Value;

@Value
public class Response {

    long clientId;

    Status status;

    public enum Status {
        SUCCESS,
        FAILED
    }
}
