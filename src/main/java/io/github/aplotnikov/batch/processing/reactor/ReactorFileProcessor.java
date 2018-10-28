package io.github.aplotnikov.batch.processing.reactor;

import io.github.aplotnikov.batch.processing.reactor.source.XmlFileSource;

class ReactorFileProcessor implements Runnable {

    private final XmlFileSource fileSource;

    private final XmlFileReader reader;

    private final ClientProcessor processor;

    ReactorFileProcessor(XmlFileSource fileSource, XmlFileReader reader, ClientProcessor processor) {
        this.fileSource = fileSource;
        this.reader = reader;
        this.processor = processor;
    }

    @Override
    public void run() {
        fileSource.readAll()
                  .flatMap(reader::read)
                  .map(processor::process);
        // collect responses
        // generate result file
    }
}
