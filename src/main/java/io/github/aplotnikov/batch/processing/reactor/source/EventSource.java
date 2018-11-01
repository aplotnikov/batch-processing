package io.github.aplotnikov.batch.processing.reactor.source;

import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventSource {

    Source repository;

    Source queue;

    public Flux<AbstractEvent> readAll() {
        return Flux.merge(repository.readAll(), queue.readAll());
    }
}
