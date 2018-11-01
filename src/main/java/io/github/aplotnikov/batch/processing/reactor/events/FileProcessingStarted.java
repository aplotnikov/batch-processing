package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.ToString;

@ToString
public class FileProcessingStarted extends AbstractEvent {
    public FileProcessingStarted(String sourcePath) {
        super(sourcePath);
    }
}
