package io.github.aplotnikov.batch.processing.reactor.source

import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent
import io.github.aplotnikov.batch.processing.reactor.events.FileReceived
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

class EventSourceSpec extends Specification {

    Source repository = Mock()

    Source queue = Mock()

    @Subject
    EventSource source = new EventSource(repository, queue)

    void 'should merge results from DB and from queue'() {
        given:
            AbstractEvent fileFromDB = new FileReceived('file_from_db.xml')
        and:
            AbstractEvent fileFromQueue = new FileReceived('file_from_queue.xml')
        when:
            Flux<AbstractEvent> files = source.readAll()
        then:
            StepVerifier.create(files)
                    .expectNext(fileFromDB)
                    .expectNext(fileFromQueue)
        and:
            1 * repository.readAll() >> Flux.just(fileFromDB)
        and:
            1 * queue.readAll() >> Flux.just(fileFromQueue)
        and:
            0 * _
    }
}
