package io.github.aplotnikov.batch.processing.reactor;

import io.vavr.control.Try;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

class XmlFileReader {

    Flux<Client> read(String path) {
        return Flux.generate(
                reader(path),
                this::findClients,
                close()
        );
    }

    private Callable<XMLStreamReader> reader(String path) {
        return () -> {
            BufferedInputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream(path));
            return XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
        };
    }

    private XMLStreamReader findClients(XMLStreamReader reader, SynchronousSink<Client> sink) {
        Try.run(() -> {
            // Need to think about refactoring it to Stream
            while (reader.hasNext()) {
                if (reader.next() == START_ELEMENT && reader.getLocalName().equals("client")) {
                    sink.next(parseClient(reader));
                }
            }
        })
           .onFailure(sink::error)
           .onSuccess(aVoid -> sink.complete());
        return reader;
    }

    private Client parseClient(XMLStreamReader reader) {
        return new Client(
                Long.parseLong(reader.getAttributeValue("", "id"))
        );
    }

    private Consumer<XMLStreamReader> close() {
        return reader -> Try.run(reader::close).get();
    }
}
