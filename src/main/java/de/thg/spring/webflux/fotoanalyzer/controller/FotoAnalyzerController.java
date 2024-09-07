package de.thg.spring.webflux.fotoanalyzer.controller;

import de.thg.spring.webflux.fotoanalyzer.model.Image;
import de.thg.spring.webflux.fotoanalyzer.services.FotoAnalyzerService;
import jakarta.inject.Inject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class FotoAnalyzerController {

    @Inject
    private FotoAnalyzerService service;

    @GetMapping
    public String hello() {
        return "FotoAnalyzer WebFlux";
    }

    @PostMapping(value = "analyze_image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> analyseImage(@RequestPart("file") Mono<FilePart> filePartMono) {
        return filePartMono
                .flatMap(this::retrieveMetadata)
                .map(opt -> {
                    if(opt.isPresent()) {
                        return ResponseEntity.ok(opt.get());
                    } else {
                        return ResponseEntity.notFound().build();}
                });
    }

    @PostMapping(value = "rename_images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> renameImages(@RequestPart("files") Flux<FilePart> filePartFlux) {
        return filePartFlux.flatMap(fp -> {
                    String filename = fp.filename();
                    try (InputStream inputStream = getInputStreamFromFluxDataBuffer(fp.content())) {
                        return Mono.just(service.createNameByCreationDate(inputStream, filename));
                    } catch (IOException e) {
                        return Flux.error(new RuntimeException(e));
                    }
                }).filter(Optional::isPresent).map(Optional::get)
                .collect(HashMap::new, Map::putAll)
                .map(item -> {
                    if (item.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    } else {
                        return ResponseEntity.ok(item);
                    }
                });
    }

    private Mono<Optional<Image>> retrieveMetadata(FilePart filePart) {
        String filename = filePart.filename();
        try(InputStream inputStream = getInputStreamFromFluxDataBuffer(filePart.content())) {
            return Mono.just(service.analyseImage(inputStream, filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getInputStreamFromFluxDataBuffer(Flux<DataBuffer> data) throws IOException {
        PipedOutputStream osPipe = new PipedOutputStream();
        PipedInputStream isPipe = new PipedInputStream(osPipe);

        DataBufferUtils.write(data, osPipe)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete(() -> {
                    try {
                        osPipe.close();
                    } catch (IOException ignored) {
                    }
                })
                .subscribe(DataBufferUtils.releaseConsumer());
        return isPipe;
    }
}
