package io.github.aplotnikov.batch.processing.reactor.events

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class FileProcessedSpec extends Specification {
    void 'equals and hashcode contract should be followed'() {
        expect:
            EqualsVerifier.forClass(FileProcessed)
                    .usingGetClass()
                    .withRedefinedSuperclass()
                    .verify()
    }
}
