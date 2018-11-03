package io.github.aplotnikov.batch.processing.reactor;

import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import io.github.aplotnikov.batch.processing.reactor.readers.FileReader;
import io.github.aplotnikov.batch.processing.reactor.source.EventSource;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class ReactorFileProcessor implements Runnable {

    EventSource fileSource;

    FileReader reader;

    EventProcessor processor;

    EventWriter writer;

    @Override
    public void run() {
        fileSource.readAll()
                  .flatMap(reader::read)
                  .map(processor::process)
                  .groupBy(AbstractEvent::getSourcePath)
                  .subscribe(writer::write);
    }
}
