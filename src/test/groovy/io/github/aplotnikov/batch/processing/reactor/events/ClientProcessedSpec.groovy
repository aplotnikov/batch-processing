package io.github.aplotnikov.batch.processing.reactor.events

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class ClientProcessedSpec extends Specification {
    void 'equals and hashcode contract should be followed'() {
        expect:
            EqualsVerifier.forClass(ClientProcessed)
                    .usingGetClass()
                    .withRedefinedSuperclass()
                    .verify()
    }
}
