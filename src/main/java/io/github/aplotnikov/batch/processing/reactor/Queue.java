package io.github.aplotnikov.batch.processing.reactor;

import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.locks.LockSupport.parkNanos;

class Queue {

    private final int processedFileNumber;

    private final int pause;

    Queue(int processedFileNumber, int pause) {
        this.processedFileNumber = processedFileNumber;
        this.pause = pause;
    }

    Flux<String> poll() {
        AtomicInteger processedFiles = new AtomicInteger(0);
        return Flux.generate(
                sink -> {
                    if (processedFiles.getAndIncrement() < processedFileNumber) {
                        sink.next("client.xml");
                    } else {
                        sink.complete();
                    }
                    parkNanos(SECONDS.toNanos(pause));
                });
    }
}
