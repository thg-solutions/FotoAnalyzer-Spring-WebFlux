package de.thg.spring.webflux.fotoanalyzer.services;

import de.thg.spring.webflux.fotoanalyzer.model.Image;

import java.io.InputStream;

public interface ImageMetadataReader {

    Image readImageMetadata(InputStream inputStream, String originalName);

}
