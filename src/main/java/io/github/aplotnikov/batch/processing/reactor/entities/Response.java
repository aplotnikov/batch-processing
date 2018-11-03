package io.github.aplotnikov.batch.processing.reactor.entities;

import lombok.Value;
import net.jcip.annotations.Immutable;

@Immutable
@Value
public final class Response {

    long clientId;

    Status status;

    public enum Status {
        SUCCESS,
        FAILED
    }
}
