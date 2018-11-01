package io.github.aplotnikov.batch.processing.reactor;

import io.github.aplotnikov.batch.processing.reactor.events.ClientParsed;
import io.github.aplotnikov.batch.processing.reactor.events.ClientProcessed;
import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class EventProcessor {

    ClientProcessor processor;

    AbstractEvent process(AbstractEvent incomeEvent) {
        return Match(incomeEvent).of(
                Case($(instanceOf(ClientParsed.class)),
                     event -> new ClientProcessed(
                             event.getSourcePath(),
                             processor.process(event.getClient())
                     )
                ),
                Case($(), incomeEvent)
        );
    }
}
