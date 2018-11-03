package io.github.aplotnikov.batch.processing.reactor.events;

import io.github.aplotnikov.batch.processing.reactor.entities.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import net.jcip.annotations.Immutable;

import static lombok.AccessLevel.PRIVATE;

@Immutable
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class ClientParsed extends AbstractEvent {

    Client client;

    public ClientParsed(String sourcePath, Client client) {
        super(sourcePath);
        this.client = client;
    }
}
