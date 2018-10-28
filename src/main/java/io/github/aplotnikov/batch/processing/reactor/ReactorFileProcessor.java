package io.github.aplotnikov.batch.processing.reactor;

import reactor.core.publisher.Flux;

class ReactorFileProcessor implements Runnable {

    private final Repository repository;

    private final Queue queue;

    private final XmlFileReader reader;

    ReactorFileProcessor(Repository repository, Queue queue, XmlFileReader reader) {
        this.repository = repository;
        this.queue = queue;
        this.reader = reader;
    }

    @Override
    public void run() {
        Flux.merge(repository.readAll(), queue.poll())
            .flatMap(reader::read);
        // generate responses with pause
        // collect responses
        // generate result file
    }
}
