package io.github.aplotnikov.batch.processing.reactor.source;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class XmlFilesSource {

    Source repository;

    Source queue;

    public Flux<String> readAll() {
        return Flux.merge(repository.readAll(), queue.readAll());
    }
}
