package io.github.aplotnikov.batch.processing.reactor.readers;

import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import reactor.core.publisher.Flux;

public interface FileReader {

    Flux<AbstractEvent> read(AbstractEvent event);

}
