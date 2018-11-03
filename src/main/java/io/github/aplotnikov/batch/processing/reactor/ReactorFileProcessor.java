package io.github.aplotnikov.batch.processing.reactor;

import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import io.github.aplotnikov.batch.processing.reactor.readers.FileReader;
import io.github.aplotnikov.batch.processing.reactor.source.EventSource;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.jcip.annotations.ThreadSafe;

import static lombok.AccessLevel.PRIVATE;

@ThreadSafe
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
