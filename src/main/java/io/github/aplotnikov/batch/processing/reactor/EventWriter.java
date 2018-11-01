package io.github.aplotnikov.batch.processing.reactor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.aplotnikov.batch.processing.reactor.entities.Response;
import io.github.aplotnikov.batch.processing.reactor.events.ClientProcessed;
import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessed;
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessingStarted;
import io.vavr.control.Try;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.GroupedFlux;

import java.nio.file.Files;
import java.nio.file.Path;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.API.run;
import static io.vavr.Predicates.instanceOf;
import static java.nio.file.StandardOpenOption.APPEND;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
class EventWriter {

    XmlMapper xmlMapper = new XmlMapper();

    void write(GroupedFlux<String, AbstractEvent> source) {
        source.subscribe(
                event -> process(Path.of("response_" + source.key()), event)
        );
    }

    private void process(Path targetPath, AbstractEvent incomeEvent) {
        Match(incomeEvent).of(
                Case(
                        $(instanceOf(FileProcessingStarted.class)),
                        event -> run(() -> writeContent(targetPath, "<responses>"))
                ),
                Case(
                        $(instanceOf(FileProcessed.class)),
                        event -> run(() -> writeContent(targetPath, "</responses>"))
                ),
                Case(
                        $(instanceOf(ClientProcessed.class)),
                        event -> run(() -> writeResponse(targetPath, event.getResponse()))
                )
        );
    }

    private void writeContent(Path path, String content) {
        Try.run(() -> Files.writeString(path, content, APPEND));
    }

    private void writeResponse(Path path, Response response) {
        Try.run(() -> {
            String content = xmlMapper.writeValueAsString(response);
            writeContent(path, content);
        });
    }
}
