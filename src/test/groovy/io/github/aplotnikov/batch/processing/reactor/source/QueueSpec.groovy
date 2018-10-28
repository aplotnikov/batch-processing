package io.github.aplotnikov.batch.processing.reactor.source

import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

class QueueSpec extends Specification {

    @Subject
    Queue queue = new Queue(2, 0)

    void 'should generate 2 files into Flux and Flux should be completed'() {
        expect:
            StepVerifier.create(queue.poll())
                    .expectNext('client.xml')
                    .expectNext('client.xml')
                    .expectComplete()
                    .verify()
    }
}
