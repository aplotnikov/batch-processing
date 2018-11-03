package io.github.aplotnikov.batch.processing.reactor.events

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class ClientParsedSpec extends Specification {
    void 'equals and hashcode contract should be followed'() {
        expect:
            EqualsVerifier.forClass(ClientParsed)
                    .usingGetClass()
                    .withRedefinedSuperclass()
                    .verify()
    }
}
