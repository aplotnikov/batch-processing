package io.github.aplotnikov.batch.processing.reactor.source;

import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import io.github.aplotnikov.batch.processing.reactor.events.FileReceived;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.jcip.annotations.ThreadSafe;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.locks.LockSupport.parkNanos;
import static lombok.AccessLevel.PRIVATE;

@ThreadSafe
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class Source {

    int processedFileNumber;

    int pause;

    Flux<AbstractEvent> readAll() {
        AtomicInteger processedFiles = new AtomicInteger(0);
        return Flux.generate(
                sink -> {
                    parkNanos(SECONDS.toNanos(pause));
                    if (processedFiles.getAndIncrement() < processedFileNumber) {
                        sink.next(
                                new FileReceived("client.xml")
                        );
                    } else {
                        sink.complete();
                    }
                });
    }
}
