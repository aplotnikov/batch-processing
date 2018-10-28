package io.github.aplotnikov.batch.processing.reactor.source

import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration
import java.time.Instant

class SourceSpec extends Specification {

    @Subject
    Source source = new Source(2, 0)

    void 'should generate 2 files into Flux and Flux should be completed'() {
        expect:
            StepVerifier.create(source.readAll())
                    .expectNext('client.xml')
                    .expectNext('client.xml')
                    .expectComplete()
                    .verify()
    }

    void 'should generate 0 files into Flux and Flux should be completed when it is configured in the constructor'() {
        given:
            source = new Source(0, 0)
        expect:
            StepVerifier.create(source.readAll())
                    .expectComplete()
                    .verify()
    }

    void 'should execution take at least 2 seconds as it is specified in the constructor'() {
        given:
            int pause = 2
        and:
            source = new Source(1, pause)
        and:
            Instant start = Instant.now()
        when:
            StepVerifier.create(source.readAll())
                    .expectNext('client.xml')
                    .expectComplete()
                    .verify()
        then:
            Instant finish = Instant.now()
            Duration.between(start, finish).toSeconds() >= pause
    }
}
