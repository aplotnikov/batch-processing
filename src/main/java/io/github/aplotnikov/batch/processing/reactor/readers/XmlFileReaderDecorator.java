package io.github.aplotnikov.batch.processing.reactor.readers;

import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.jcip.annotations.ThreadSafe;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

import static lombok.AccessLevel.PRIVATE;

@ThreadSafe
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class XmlFileReaderDecorator implements FileReader {

    Path rootSourceFolder;

    @Override
    public Flux<AbstractEvent> read(AbstractEvent event) {
        return new XmlFileReader(rootSourceFolder).read(event);
    }
}
