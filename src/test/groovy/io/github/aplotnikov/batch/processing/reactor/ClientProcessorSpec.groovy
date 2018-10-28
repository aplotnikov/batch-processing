package io.github.aplotnikov.batch.processing.reactor

import static io.github.aplotnikov.batch.processing.reactor.Response.Status.FAILED
import static io.github.aplotnikov.batch.processing.reactor.Response.Status.SUCCESS
import static java.util.concurrent.TimeUnit.SECONDS

import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration
import java.time.Instant
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

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
                status == SUCCESS
            }
        when:
            response = processor.process(thirdClient)
        then:
            with(response) {
                clientId == thirdClient.id
                status == FAILED
            }
    }

    void 'should execution take at least 2 seconds as it is specified in the constructor'() {
        given:
            int pause = 2
        and:
            processor = new ClientProcessor(pause)
        and:
            Client client = new Client(1)
        and:
            Instant start = Instant.now()
        when:
            Response response = processor.process(client)
        then:
            Instant finish = Instant.now()
            Duration.between(start, finish).toSeconds() >= pause
        and:
            with(response) {
                clientId == client.id
                status == FAILED
            }
    }

    void 'should work correctly under concurrent usage'() {
        given:
            int threadNumber = 10
        and:
            ExecutorService threadPool = Executors.newFixedThreadPool(threadNumber)
        and:
            CountDownLatch afterInitBlocker = new CountDownLatch(threadNumber)
            CountDownLatch allDone = new CountDownLatch(threadNumber)
        and:
            AtomicInteger clientNumber = new AtomicInteger(1)
        and:
            List<Response> actualResponses = [].asSynchronized()
        and:
            Runnable action = {
                int id = clientNumber.getAndIncrement()

                afterInitBlocker.countDown()
                afterInitBlocker.await()

                actualResponses << processor.process(new Client(id))

                allDone.countDown()
            }
        when:
            try {
                (1..threadNumber).each {
                    threadPool.submit(action)
                }
                allDone.await(10, SECONDS)
            } finally {
                threadPool.shutdown()
            }
        then:
            actualResponses.size() == threadNumber
            actualResponses.findAll { it.status == SUCCESS }.size() == threadNumber / 2
    }
}
