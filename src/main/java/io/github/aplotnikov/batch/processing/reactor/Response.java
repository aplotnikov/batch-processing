package io.github.aplotnikov.batch.processing.reactor;

import lombok.Value;

@Value
class Response {

    long clientId;

    Status status;

    enum Status {
        SUCCESS,
        FAILED
    }
}
