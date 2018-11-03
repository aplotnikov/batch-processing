package io.github.aplotnikov.batch.processing.reactor.events;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.jcip.annotations.Immutable;

@Immutable
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class FileReceived extends AbstractEvent {
    public FileReceived(String sourcePath) {
        super(sourcePath);
    }
}
