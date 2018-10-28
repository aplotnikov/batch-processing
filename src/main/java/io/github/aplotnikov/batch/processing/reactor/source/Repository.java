package io.github.aplotnikov.batch.processing.reactor.source;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.locks.LockSupport.parkNanos;
import static java.util.stream.IntStream.range;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class Repository {

    int processedFileNumber;

    int pause;

    Flux<String> readAll() {
        return Flux.generate(
                sink -> {
                    parkNanos(SECONDS.toNanos(pause));
                    range(0, processedFileNumber).forEach(index -> sink.next("client.xml"));
                    sink.complete();
                });
    }
}
