package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class FileReceived extends AbstractEvent {
    public FileReceived(String sourcePath) {
        super(sourcePath);
    }
}
