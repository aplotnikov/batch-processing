package io.github.aplotnikov.batch.processing.reactor

import static io.github.aplotnikov.batch.processing.reactor.entities.Response.Status.SUCCESS

import io.github.aplotnikov.batch.processing.reactor.entities.Client
import io.github.aplotnikov.batch.processing.reactor.entities.Response
import io.github.aplotnikov.batch.processing.reactor.events.AbstractEvent
import io.github.aplotnikov.batch.processing.reactor.events.ClientParsed
import io.github.aplotnikov.batch.processing.reactor.events.ClientProcessed
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessed
import io.github.aplotnikov.batch.processing.reactor.events.FileProcessingStarted
import io.github.aplotnikov.batch.processing.reactor.events.FileReceived
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Unroll
class EventProcessorSpec extends Specification {

    static final String ANY_PATH = 'path.xml'

    ClientProcessor clientProcessor = Mock()

    @Subject
    EventProcessor processor = new EventProcessor(clientProcessor)

    void 'should process ClientParsed event to ClientProcessed event'() {
        given:
            ClientParsed clientParsedEvent = new ClientParsed(ANY_PATH, new Client(1))
        and:
            Response processedResponse = new Response(clientParsedEvent.client.id, SUCCESS)
        when:
            AbstractEvent event = processor.process(clientParsedEvent)
        then:
            event instanceof ClientProcessed
        and:
            with(event) {
                fileName == clientParsedEvent.fileName
                response == processedResponse
            }
        and:
            1 * clientProcessor.process(clientParsedEvent.client) >> processedResponse
    }

    void 'should keep the same event if it is not ClientParsed event'() {
        when:
            AbstractEvent processedEvent = processor.process(event)
        then:
            processedEvent == event
        and:
            0 * _
        where:
            event << [
                    new FileReceived(ANY_PATH),
                    new FileProcessingStarted(ANY_PATH),
                    new FileProcessed(ANY_PATH),
            ]
    }
}
