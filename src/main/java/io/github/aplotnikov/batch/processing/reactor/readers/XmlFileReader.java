package io.github.aplotnikov.batch.processing.reactor.readers;

import io.github.aplotnikov.batch.processing.reactor.entities.Client;
import io.github.aplotnikov.batch.processing.reactor.entities.Client.ClientBuilder;
import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import io.github.aplotnikov.batch.processing.reactor.events.ClientParsed;
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessed;
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessingStarted;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.jcip.annotations.NotThreadSafe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.util.annotation.Nullable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Locale.ENGLISH;
import static java.util.Objects.nonNull;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static lombok.AccessLevel.PRIVATE;

@NotThreadSafe
@FieldDefaults(level = PRIVATE)
@RequiredArgsConstructor
public final class XmlFileReader implements FileReader {

    final String rootSourceFolder;

    @Nullable
    ClientBuilder builder;

    String fileName;

    @Override
    public Flux<AbstractEvent> read(AbstractEvent event) {
        this.fileName = event.getFileName();
        return Flux.concat(
                Flux.just(new FileProcessingStarted(fileName)),
                Flux.generate(
                        reader(rootSourceFolder + File.separator + fileName),
                        this::findClients,
                        closeReader()
                ),
                Flux.just(new FileProcessed(fileName))
        );
    }

    private Callable<XMLStreamReader> reader(String filePath) {
        return () -> {
            Reader reader = Files.newBufferedReader(Path.of(filePath));
            return XMLInputFactory.newInstance().createXMLStreamReader(reader);
        };
    }

    private Consumer<XMLStreamReader> closeReader() {
        return reader -> Try.run(reader::close);
    }

    private XMLStreamReader findClients(XMLStreamReader reader, SynchronousSink<AbstractEvent> sink) {
        Try.run(() -> parseClients(reader, sink)).onFailure(sink::error);
        return reader;
    }

    private void parseClients(XMLStreamReader reader, SynchronousSink<AbstractEvent> sink) throws XMLStreamException {
        boolean anyActionOnSinkIsDone = false;

        while (reader.hasNext() && !anyActionOnSinkIsDone) {
            switch (reader.next()) {
                case START_ELEMENT:
                    processStartElement(reader);
                    break;
                case END_ELEMENT:
                    anyActionOnSinkIsDone = processEndElement(reader, sink);
                    break;
                case END_DOCUMENT:
                    sink.complete();
                    anyActionOnSinkIsDone = true;
                    break;
                default:
            }
        }
    }

    private void processStartElement(XMLStreamReader reader) throws XMLStreamException {
        switch (XmlTag.of(reader.getLocalName())) {
            case CLIENT:
                builder = Client.builder();
                break;
            case ID:
                if (nonNull(builder)) {
                    builder.id(Long.parseLong(reader.getElementText()));
                }
                break;
            default:
        }
    }

    private boolean processEndElement(XMLStreamReader reader, SynchronousSink<AbstractEvent> sink) {
        if (XmlTag.of(reader.getLocalName()).equals(XmlTag.CLIENT) && nonNull(builder)) {
            sink.next(
                    new ClientParsed(fileName, builder.build())
            );
            builder = null;
            return true;
        }

        return false;
    }

    private enum XmlTag {
        UNKNOWN,
        CLIENT,
        ID;

        static XmlTag of(String text) {
            return Stream.of(values())
                         .filter(value -> value.name().equals(text.toUpperCase(ENGLISH)))
                         .findAny()
                         .orElse(UNKNOWN);
        }
    }
}
