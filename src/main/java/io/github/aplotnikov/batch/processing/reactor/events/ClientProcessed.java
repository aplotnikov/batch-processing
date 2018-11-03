package io.github.aplotnikov.batch.processing.reactor.events;

import io.github.aplotnikov.batch.processing.reactor.entities.Response;
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
public final class ClientProcessed extends AbstractEvent {

    Response response;

    public ClientProcessed(String sourcePath, Response response) {
        super(sourcePath);
        this.response = response;
    }
}
