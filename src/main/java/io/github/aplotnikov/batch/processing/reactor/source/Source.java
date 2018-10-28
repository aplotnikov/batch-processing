package io.github.aplotnikov.batch.processing.reactor.source;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.locks.LockSupport.parkNanos;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class Source {

    int processedFileNumber;

    int pause;

    Flux<String> readAll() {
        AtomicInteger processedFiles = new AtomicInteger(0);
        return Flux.generate(
                sink -> {
                    parkNanos(SECONDS.toNanos(pause));
                    if (processedFiles.getAndIncrement() < processedFileNumber) {
                        sink.next("client.xml");
                    } else {
                        sink.complete();
                    }
                });
    }
}
