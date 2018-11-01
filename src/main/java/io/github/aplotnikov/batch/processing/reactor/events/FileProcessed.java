package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class FileProcessed extends AbstractEvent {
    public FileProcessed(String sourcePath) {
        super(sourcePath);
    }
}
