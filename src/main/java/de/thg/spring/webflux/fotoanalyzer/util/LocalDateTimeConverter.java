package de.thg.spring.webflux.fotoanalyzer.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeConverter {

    @Value("${photoalbum.extension:jpg}")
    String extension;

    private static final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
    private static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public LocalDateTime toLocalDateTime(String stringRepresentation) {
        return LocalDateTime.parse(stringRepresentation, formatter1);
    }

    public String toFilename(LocalDateTime ldt) {
        return ldt.format(formatter2) + "." + extension;
    }

    public String toFilename(String stringRepresentation) {
        return LocalDateTime.parse(stringRepresentation, formatter1).format(formatter2) + "." + extension;
    }
}
