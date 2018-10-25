package io.github.aplotnikov.batch.processing.reactor;

import reactor.core.publisher.Flux;

class ReactorFileProcessor implements Runnable {

    private final Repository repository;

    private final Queue queue;

    ReactorFileProcessor(Repository repository, Queue queue) {
        this.repository = repository;
        this.queue = queue;
    }

    @Override
    public void run() {
        Flux.merge(repository.readAll(), queue.poll());
        // process XML file
        // generate responses with pause
        // collect responses
        // generate result file
    }
}
