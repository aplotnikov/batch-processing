package io.github.aplotnikov.batch.processing.reactor

import static io.github.aplotnikov.batch.processing.reactor.Response.Status.FAILED

import spock.lang.Specification
import spock.lang.Subject

class ClientProcessorSpec extends Specification {

    @Subject
    ClientProcessor processor = new ClientProcessor(0)

    void 'should process clients with different statuses'() {
        given:
            Client firstClient = new Client(1)
        and:
            Client secondClient = new Client(2)
        and:
            Client thirdClient = new Client(3)
        when:
            Response response = processor.process(firstClient)
        then:
            with(response) {
                clientId == firstClient.id
                status == FAILED
            }
        when:
            response = processor.process(secondClient)
        then:
            with(response) {
                clientId == secondClient.id
                status == FAILED
            }
        when:
            response = processor.process(thirdClient)
        then:
            with(response) {
                clientId == thirdClient.id
                status == FAILED
            }
    }
}
