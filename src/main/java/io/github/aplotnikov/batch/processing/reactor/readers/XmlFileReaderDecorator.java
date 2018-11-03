package io.github.aplotnikov.batch.processing.reactor.readers;

import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import reactor.core.publisher.Flux;

public final class XmlFileReaderDecorator implements FileReader {
    @Override
    public Flux<AbstractEvent> read(AbstractEvent event) {
        return new XmlFileReader().read(event);
    }
}
