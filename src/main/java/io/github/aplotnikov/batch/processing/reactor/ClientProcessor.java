package io.github.aplotnikov.batch.processing.reactor;

import java.util.concurrent.atomic.AtomicBoolean;

import static io.github.aplotnikov.batch.processing.reactor.Response.Status.FAILED;
import static io.github.aplotnikov.batch.processing.reactor.Response.Status.SUCCESS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.locks.LockSupport.parkNanos;

class ClientProcessor {

    private final AtomicBoolean isLastResponseSuccessful = new AtomicBoolean();

    private final int pause;

    ClientProcessor(int pause) {
        this.pause = pause;
    }

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
        } while (isLastResponseSuccessful.compareAndExchange(isSuccessful, !isSuccessful));

        return isSuccessful;
    }
}
