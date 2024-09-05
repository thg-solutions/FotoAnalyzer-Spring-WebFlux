package de.thg.spring.webflux.fotoanalyzer.services;

import de.thg.spring.webflux.fotoanalyzer.model.Image;
import de.thg.spring.webflux.fotoanalyzer.util.LocalDateTimeConverter;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FotoAnalyzerService {

    private final ImageMetadataReader imageMetadataReader;
    private final LocalDateTimeConverter localDateTimeConverter;

    @Inject
    public FotoAnalyzerService(ImageMetadataReader imageMetadataReader, LocalDateTimeConverter localDateTimeConverter) {
        this.imageMetadataReader = imageMetadataReader;
        this.localDateTimeConverter = localDateTimeConverter;
    }

    public Optional<Image> analyseImage(InputStream inputStream, String originalName) {
        return Optional.ofNullable(imageMetadataReader.readImageMetadata(inputStream, originalName));
    }

    public Optional<Map<String, String>> createNameByCreationDate(InputStream inputStream, String originalName) {
        Optional<Image> image = analyseImage(inputStream, originalName);
        return image.map(value -> Map.of(value.getFilename(), localDateTimeConverter.toFilename(value.getCreationDate())));
    }

}
