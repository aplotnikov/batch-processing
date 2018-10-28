package io.github.aplotnikov.batch.processing.reactor.source;

import reactor.core.publisher.Flux;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.locks.LockSupport.parkNanos;
import static java.util.stream.IntStream.range;

class Repository {

    private final int processedFileNumber;

    private final int pause;

    Repository(int processedFileNumber, int pause) {
        this.processedFileNumber = processedFileNumber;
        this.pause = pause;
    }

    Flux<String> readAll() {
        return Flux.generate(
                sink -> {
                    parkNanos(SECONDS.toNanos(pause));
                    range(0, processedFileNumber).forEach(index -> sink.next("client.xml"));
                    sink.complete();
                });
    }
}
