package io.github.aplotnikov.batch.processing.reactor.source;

import reactor.core.publisher.Flux;

public class XmlFileSource {

    private final Repository repository;

    private final Queue queue;

    public XmlFileSource(Repository repository, Queue queue) {
        this.repository = repository;
        this.queue = queue;
    }

    public Flux<String> readAll() {
        return Flux.merge(repository.readAll(), queue.poll());
    }
}
