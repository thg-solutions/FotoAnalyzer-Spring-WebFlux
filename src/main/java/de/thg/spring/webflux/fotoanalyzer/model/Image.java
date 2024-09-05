package de.thg.spring.webflux.fotoanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Image implements Comparable<Image> {

    private String id;

    private LocalDateTime creationDate;

    private Double latitude;

    private Double longitude;

    private LocalDateTime lastModified;

    private String filename;

    public Image() {

    }

    public Image(String filename, LocalDateTime creationDate) {
        this.filename = filename;
        this.creationDate = creationDate;
    }

    @Override
    public int compareTo(Image o) {
        return this.creationDate.compareTo(o.creationDate);
    }
}
