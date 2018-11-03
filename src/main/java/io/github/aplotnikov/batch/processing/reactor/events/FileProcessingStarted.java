package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.jcip.annotations.Immutable;

@Immutable
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class FileProcessingStarted extends AbstractEvent {
    public FileProcessingStarted(String sourcePath) {
        super(sourcePath);
    }
}
