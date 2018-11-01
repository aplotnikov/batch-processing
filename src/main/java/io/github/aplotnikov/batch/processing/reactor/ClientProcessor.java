package io.github.aplotnikov.batch.processing.reactor;

import io.github.aplotnikov.batch.processing.reactor.entities.Client;
import io.github.aplotnikov.batch.processing.reactor.entities.Response;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.aplotnikov.batch.processing.reactor.entities.Response.Status.FAILED;
import static io.github.aplotnikov.batch.processing.reactor.entities.Response.Status.SUCCESS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.locks.LockSupport.parkNanos;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class ClientProcessor {

    AtomicBoolean isLastResponseSuccessful = new AtomicBoolean();

    int pause;

    Response process(Client client) {
        parkNanos(SECONDS.toNanos(pause));
        return new Response(
                client.getId(),
                isCurrentResponseSuccessful() ? SUCCESS : FAILED
        );
    }

    private boolean isCurrentResponseSuccessful() {
        boolean isSuccessful;

        do {
            isSuccessful = isLastResponseSuccessful.get();
        } while (!isLastResponseSuccessful.compareAndSet(isSuccessful, !isSuccessful));

        return isSuccessful;
    }
}
