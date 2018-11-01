package io.github.aplotnikov.batch.processing.reactor.events;

import io.github.aplotnikov.batch.processing.reactor.entities.Response;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@Getter
@ToString
public class ClientProcessed extends AbstractEvent {

    Response response;

    public ClientProcessed(String sourcePath, Response response) {
        super(sourcePath);
        this.response = response;
    }
}
