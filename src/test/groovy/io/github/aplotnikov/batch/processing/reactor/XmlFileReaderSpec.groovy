package io.github.aplotnikov.batch.processing.reactor

import io.github.aplotnikov.batch.processing.reactor.entities.Client
import io.github.aplotnikov.batch.processing.reactor.events.ClientParsed
import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessed
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessingStarted
import io.github.aplotnikov.batch.processing.reactor.events.FileReceived
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import javax.xml.stream.XMLStreamException

class XmlFileReaderSpec extends Specification {

    @Subject
    XmlFileReader reader = new XmlFileReader()

    void 'should read all clients from xml file'() {
        given:
            AbstractEvent event = new FileReceived('clients.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.sourcePath))
                    .expectNext(new ClientParsed(event.sourcePath, new Client(1)))
                    .expectNext(new ClientParsed(event.sourcePath, new Client(2)))
                    .expectNext(new FileProcessed(event.sourcePath))
                    .expectComplete()
                    .verify()
    }

    void 'should fail processing file'() {
        given:
            AbstractEvent event = new FileReceived('empty_clients.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.sourcePath))
                    .expectError()
                    .verify()
    }

    void 'should read all clients even when they are in upper and lower cases'() {
        given:
            AbstractEvent event = new FileReceived('lower_and_upper_case_clients.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.sourcePath))
                    .expectNext(new ClientParsed(event.sourcePath, new Client(1)))
                    .expectNext(new ClientParsed(event.sourcePath, new Client(2)))
                    .expectNext(new ClientParsed(event.sourcePath, new Client(3)))
                    .expectNext(new FileProcessed(event.sourcePath))
                    .expectComplete()
                    .verify()
    }

    void 'should complete flux when there are no tags for which the actions are configured'() {
        given:
            AbstractEvent event = new FileReceived('no_client_tags.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.sourcePath))
                    .expectNext(new FileProcessed(event.sourcePath))
                    .expectComplete()
                    .verify()
    }

    void 'should complete flux when there are client specific tags outside of the client'() {
        given:
            AbstractEvent event = new FileReceived('client_inner_tags_outside_clients.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.sourcePath))
                    .expectNext(new FileProcessed(event.sourcePath))
                    .expectComplete()
                    .verify()
    }

    void 'should read empty client when there are no information about client'() {
        given:
            AbstractEvent event = new FileReceived('empty_client.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.sourcePath))
                    .expectNext(new ClientParsed(event.sourcePath, new Client(0)))
                    .expectNext(new FileProcessed(event.sourcePath))
                    .expectComplete()
                    .verify()
    }

    void 'should fail processing invalid xml file'() {
        given:
            AbstractEvent event = new FileReceived('no_closed_client_tag.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.sourcePath))
                    .expectError(XMLStreamException)
                    .verify()
    }
}
