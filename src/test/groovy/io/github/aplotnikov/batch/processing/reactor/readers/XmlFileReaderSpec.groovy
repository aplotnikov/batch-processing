package io.github.aplotnikov.batch.processing.reactor.readers

import io.github.aplotnikov.batch.processing.reactor.entities.Client
import io.github.aplotnikov.batch.processing.reactor.events.ClientParsed
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessed
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessingStarted
import io.github.aplotnikov.batch.processing.reactor.events.FileReceived
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import javax.xml.stream.XMLStreamException
import java.nio.file.Path

class XmlFileReaderSpec extends Specification {

    @Subject
    XmlFileReader reader = new XmlFileReader(rootFolder())

    void 'should read all clients from xml file'() {
        given:
            FileReceived event = new FileReceived('clients.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.fileName))
                    .expectNext(new ClientParsed(event.fileName, new Client(1)))
                    .expectNext(new ClientParsed(event.fileName, new Client(2)))
                    .expectNext(new FileProcessed(event.fileName))
                    .expectComplete()
                    .verify()
    }

    void 'should fail processing file'() {
        given:
            FileReceived event = new FileReceived('empty_clients.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.fileName))
                    .expectError()
                    .verify()
    }

    void 'should read all clients even when they are in upper and lower cases'() {
        given:
            FileReceived event = new FileReceived('lower_and_upper_case_clients.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.fileName))
                    .expectNext(new ClientParsed(event.fileName, new Client(1)))
                    .expectNext(new ClientParsed(event.fileName, new Client(2)))
                    .expectNext(new ClientParsed(event.fileName, new Client(3)))
                    .expectNext(new FileProcessed(event.fileName))
                    .expectComplete()
                    .verify()
    }

    void 'should complete flux when there are no tags for which the actions are configured'() {
        given:
            FileReceived event = new FileReceived('no_client_tags.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.fileName))
                    .expectNext(new FileProcessed(event.fileName))
                    .expectComplete()
                    .verify()
    }

    void 'should complete flux when there are client specific tags outside of the client'() {
        given:
            FileReceived event = new FileReceived('client_inner_tags_outside_clients.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.fileName))
                    .expectNext(new FileProcessed(event.fileName))
                    .expectComplete()
                    .verify()
    }

    void 'should read empty client when there are no information about client'() {
        given:
            FileReceived event = new FileReceived('empty_client.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.fileName))
                    .expectNext(new ClientParsed(event.fileName, new Client(0)))
                    .expectNext(new FileProcessed(event.fileName))
                    .expectComplete()
                    .verify()
    }

    void 'should fail processing invalid xml file'() {
        given:
            FileReceived event = new FileReceived('no_closed_client_tag.xml')
        expect:
            StepVerifier.create(reader.read(event))
                    .expectNext(new FileProcessingStarted(event.fileName))
                    .expectError(XMLStreamException)
                    .verify()
    }

    private String rootFolder() {
        // Think to improve it
        // File clients.xml should exist into the same folder as this tests file
        Path.of(getClass().getResource('clients.xml').path)
                .parent.toFile().absolutePath
    }
}
