package io.github.aplotnikov.batch.processing.reactor

import io.github.aplotnikov.batch.processing.reactor.events.FileReceived
import io.github.aplotnikov.batch.processing.reactor.readers.XmlFileReaderDecorator
import io.github.aplotnikov.batch.processing.reactor.source.EventSource
import io.github.aplotnikov.batch.processing.reactor.source.Source
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import reactor.core.publisher.Flux
import spock.lang.Specification
import spock.lang.Subject
import spock.util.concurrent.PollingConditions

import java.time.Duration
import java.time.Instant

class ReactorFileProcessorSpec extends Specification {

    @Rule
    TemporaryFolder folder

    Source db = Stub()

    Source queue = Stub()

    @Subject
    ReactorFileProcessor processor

    void setup() {
        processor = new ReactorFileProcessor(
                new EventSource(db, queue),
                new XmlFileReaderDecorator(folder.root.toPath()),
                new EventProcessor(new ClientProcessor(1)),
                new EventWriter(folder.root.toPath())
        )
    }

    void 'should process files'() {
        given:
            PollingConditions conditions = new PollingConditions(timeout: 12)
        and:
            List<Long> clientsFromDbFile = 1..5
            String fileFromDb = generateFile(clientsFromDbFile)
        and:
            List<Long> clientsFromQueueFile = 11..15
            String fileFromQueue = generateFile(clientsFromQueueFile)
        and:
            db.readAll() >> Flux.just(new FileReceived(fileFromDb))
        and:
            queue.readAll() >> Flux.just(new FileReceived(fileFromQueue))
        when:
            processor.run()
        then:
            Duration.between(old(Instant.now()), Instant.now()).toSeconds() < 1
        and:
            conditions.eventually {
                List<File> generatedFiles = []
                folder.root.eachFileMatch(~/.*response_.*/) { generatedFiles << it }

                generatedFiles.size() == 2

                assertFileContent(
                        generatedFiles.find { it.name.contains(fileFromDb) },
                        clientsFromDbFile
                )

                assertFileContent(
                        generatedFiles.find { it.name.contains(fileFromQueue) },
                        clientsFromQueueFile
                )
            }
    }

    private static void assertFileContent(File file, List<Long> clientIds) {
        String content = file.text
        clientIds.each {
            assert content.contains("<clientId>${it}</clientId>")
        }
    }

    private String generateFile(List<Long> clientIds) {
        folder.newFile().tap {
            new FileGenerator().generate(it.toPath(), clientIds)
        }.name
    }
}
