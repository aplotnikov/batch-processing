package io.github.aplotnikov.batch.processing.reactor;

import io.github.aplotnikov.batch.processing.reactor.source.XmlFileSource;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class ReactorFileProcessor implements Runnable {

    XmlFileSource fileSource;

    XmlFileReader reader;

    ClientProcessor processor;

    @Override
    public void run() {
        fileSource.readAll()
                  .flatMap(reader::read)
                  .map(processor::process);
        // collect responses
        // generate result file
    }
}
