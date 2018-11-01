package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.ToString;

@ToString
public class FileProcessed extends AbstractEvent {
    public FileProcessed(String sourcePath) {
        super(sourcePath);
    }
}
