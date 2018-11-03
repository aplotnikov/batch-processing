package io.github.aplotnikov.batch.processing.reactor

import static java.util.concurrent.TimeUnit.SECONDS
import static java.util.concurrent.locks.LockSupport.parkNanos

import io.github.aplotnikov.batch.processing.reactor.events.FileReceived
import io.github.aplotnikov.batch.processing.reactor.readers.XmlFileReaderDecorator
import io.github.aplotnikov.batch.processing.reactor.source.EventSource
import io.github.aplotnikov.batch.processing.reactor.source.Source
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import reactor.core.publisher.Flux
import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Path

class ReactorFileProcessorSpec extends Specification {

    static final String FILE_FROM_DB = 'file_from_db.xml'

    static final String FILE_FROM_QUEUE = 'file_from_queue.xml'

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    Source db = Stub()

    Source queue = Stub()

    @Subject
    ReactorFileProcessor processor

    void setup() {
        folder.create()

        processor = new ReactorFileProcessor(
                new EventSource(db, queue),
                new XmlFileReaderDecorator(rootSourceFolder()),
                new EventProcessor(new ClientProcessor(0)),
                new EventWriter(folder.root.toPath())
        )
    }

    void 'should process files'() {
        given:
            db.readAll() >> Flux.just(new FileReceived(FILE_FROM_DB))
        and:
            queue.readAll() >> Flux.just(new FileReceived(FILE_FROM_QUEUE))
        when:
            processor.run()
        then:
            parkNanos(SECONDS.toNanos(1))
        and:
            List<File> generatedFiles = []
            folder.root.eachFileMatch(~/.*response_.*.xml/) { generatedFiles << it }
        and:
            generatedFiles.size() == 2
        and:
            File dbFileResponse = generatedFiles.find { it.absolutePath.contains(FILE_FROM_DB) }
            with(dbFileResponse.text) {
                contains '<clientId>1</clientId>'
                contains '<clientId>2</clientId>'
                contains '<clientId>3</clientId>'
                contains '<clientId>4</clientId>'
                contains '<clientId>5</clientId>'
            }
        and:
            File queueFileResponse = generatedFiles.find { it.absolutePath.contains(FILE_FROM_QUEUE) }
            with(queueFileResponse.text) {
                contains '<clientId>11</clientId>'
                contains '<clientId>12</clientId>'
                contains '<clientId>13</clientId>'
                contains '<clientId>14</clientId>'
                contains '<clientId>15</clientId>'
            }
    }

    private String rootSourceFolder() {
        // Think to improve it
        // File file_from_db.xml should exist into the same folder as this tests file
        Path.of(getClass().getResource(FILE_FROM_DB).path)
                .parent.toFile().absolutePath
    }
}
