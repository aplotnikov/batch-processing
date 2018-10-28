package io.github.aplotnikov.batch.processing.reactor.source

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

class XmlFilesSourceSpec extends Specification {

    Source repository = Mock()

    Source queue = Mock()

    @Subject
    XmlFilesSource source = new XmlFilesSource(repository, queue)

    void 'should merge results from DB and from queue'() {
        given:
            String fileFromDB = 'file_from_db.xml'
        and:
            String fileFromQueue = 'file_from_queue.xml'
        when:
            Flux<String> files = source.readAll()
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
