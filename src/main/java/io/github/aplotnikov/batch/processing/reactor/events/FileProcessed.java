package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.jcip.annotations.Immutable;

@Immutable
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class FileProcessed extends AbstractEvent {
    public FileProcessed(String sourcePath) {
        super(sourcePath);
    }
}
