package io.github.aplotnikov.batch.processing.reactor;

import io.github.aplotnikov.batch.processing.reactor.Client.ClientBuilder;
import io.vavr.control.Try;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.util.annotation.Nullable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Locale.ENGLISH;
import static java.util.Objects.nonNull;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
class XmlFileReader {

    @Nullable
    ClientBuilder builder;

    Flux<Client> read(String filePath) {
        return Flux.generate(
                reader(filePath),
                this::findClients,
                closeReader()
        );
    }

    private Callable<XMLStreamReader> reader(String filePath) {
        return () -> {
            BufferedInputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream(filePath));
            return XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        };
    }

    private Consumer<XMLStreamReader> closeReader() {
        return reader -> Try.run(reader::close);
    }

    private XMLStreamReader findClients(XMLStreamReader reader, SynchronousSink<Client> sink) {
        Try.run(() -> parseClients(reader, sink)).onFailure(sink::error);
        return reader;
    }

    private void parseClients(XMLStreamReader reader, SynchronousSink<Client> sink) throws XMLStreamException {
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

    private boolean processEndElement(XMLStreamReader reader, SynchronousSink<Client> sink) {
        if (XmlTag.of(reader.getLocalName()).equals(XmlTag.CLIENT) && nonNull(builder)) {
            sink.next(builder.build());
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
