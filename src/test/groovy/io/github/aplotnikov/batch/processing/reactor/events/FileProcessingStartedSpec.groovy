package io.github.aplotnikov.batch.processing.reactor.events

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class FileProcessingStartedSpec extends Specification {
    void 'equals and hashcode contract should be followed'() {
        expect:
            EqualsVerifier.forClass(FileProcessingStarted)
                    .usingGetClass()
                    .withRedefinedSuperclass()
                    .verify()
    }
}
