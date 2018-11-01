package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class FileProcessingStarted extends AbstractEvent {
    public FileProcessingStarted(String sourcePath) {
        super(sourcePath);
    }
}
