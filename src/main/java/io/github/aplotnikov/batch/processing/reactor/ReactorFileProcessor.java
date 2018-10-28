package io.github.aplotnikov.batch.processing.reactor;

import reactor.core.publisher.Flux;

class ReactorFileProcessor implements Runnable {

    private final Repository repository;

    private final Queue queue;

    private final XmlFileReader reader;

    private final ClientProcessor processor;

    ReactorFileProcessor(Repository repository, Queue queue, XmlFileReader reader, ClientProcessor processor) {
        this.repository = repository;
        this.queue = queue;
        this.reader = reader;
        this.processor = processor;
    }

    @Override
    public void run() {
        Flux.merge(repository.readAll(), queue.poll())
            .flatMap(reader::read)
            .map(processor::process);
        // collect responses
        // generate result file
    }
}
