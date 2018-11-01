package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.ToString;

@ToString
public class FileReceived extends AbstractEvent {
    public FileReceived(String sourcePath) {
        super(sourcePath);
    }
}
