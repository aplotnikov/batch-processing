package io.github.aplotnikov.batch.processing.reactor

import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import javax.xml.stream.XMLStreamException

class XmlFileReaderSpec extends Specification {

    @Subject
    XmlFileReader reader = new XmlFileReader()

    void 'should read all clients from xml file'() {
        expect:
            StepVerifier.create(reader.read('clients.xml'))
                    .expectNext(new Client(1), new Client(2))
                    .expectComplete()
                    .verify()
    }

    void 'should fail processing file'() {
        expect:
            StepVerifier.create(reader.read('empty_clients.xml'))
                    .expectError()
                    .verify()
    }

    void 'should read all clients even when they are in upper and lower cases'() {
        StepVerifier.create(reader.read('lower_and_upper_case_clients.xml'))
                .expectNext(new Client(1), new Client(2), new Client(3))
                .expectComplete()
                .verify()
    }

    void 'should complete flux when there are no tags for which the actions are configured'() {
        expect:
            StepVerifier.create(reader.read('no_client_tags.xml'))
                    .expectComplete()
                    .verify()
    }

    void 'should complete flux when there are client specific tags outside of the client'() {
        expect:
            StepVerifier.create(reader.read('client_inner_tags_outside_clients.xml'))
                    .expectComplete()
                    .verify()
    }

    void 'should read empty client when there are no information about client'() {
        expect:
            StepVerifier.create(reader.read('empty_client.xml'))
                    .expectNext(new Client(0))
                    .expectComplete()
                    .verify()
    }

    void 'should fail processing invalid xml file'() {
        expect:
            StepVerifier.create(reader.read('no_closed_client_tag.xml'))
                    .expectError(XMLStreamException)
                    .verify()
    }
}
