package io.github.aplotnikov.batch.processing.reactor.entities

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class ClientSpec extends Specification {
    void 'equals and hashcode contract should be followed'() {
        expect:
            EqualsVerifier.forClass(Client)
                    .usingGetClass()
                    .verify()
    }
}
