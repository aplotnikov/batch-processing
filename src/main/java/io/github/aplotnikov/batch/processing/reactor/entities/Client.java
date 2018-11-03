package io.github.aplotnikov.batch.processing.reactor.entities;

import lombok.Builder;
import lombok.Value;
import net.jcip.annotations.Immutable;

@Immutable
@Value
@Builder
public final class Client {

    long id;

}
