package io.github.aplotnikov.batch.processing.reactor

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.github.aplotnikov.batch.processing.reactor.entities.Client

import java.nio.file.Files
import java.nio.file.Path

class FileGenerator {

    private final XmlMapper xmlMapper = new XmlMapper()

    void generate(Path path, List<Long> clientIds) {
        Files.newOutputStream(path).withWriter { writer ->
            writer.write('<clients>')
            clientIds.collect { new Client(it) }
                    .collect { xmlMapper.writeValueAsString(it) }
                    .each { writer.write(it) }
            writer.write('</clients>')
        }
    }
}
