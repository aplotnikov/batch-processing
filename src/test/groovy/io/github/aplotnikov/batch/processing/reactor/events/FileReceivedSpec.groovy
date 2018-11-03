package io.github.aplotnikov.batch.processing.reactor.events

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class FileReceivedSpec extends Specification {
    void 'equals and hashcode contract should be followed'() {
        expect:
            EqualsVerifier.forClass(FileReceived)
                    .usingGetClass()
                    .withRedefinedSuperclass()
                    .verify()
    }
}
