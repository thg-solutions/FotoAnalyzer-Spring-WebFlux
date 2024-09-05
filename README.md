# FotoAnalyzer-Spring-WebFlux
WebFlux Controller for FotoAnalyzer.

Dieses Projekt verwendet Spring WebFlux für dasselbe REST-API, das für alle möglichen anderen Frameworks verwendet wird. Es sei darauf hingewiesen, dass `FotoAnalyzer` bestimmt keine geeignete Anwendung für reaktive Programmierung ist: Hier wird eine Datei hochgeladen, um ihre Metadaten mittels einer _blocking_ Operation auszulesen. Es geht hier wirklich nur im einen Versuch / ein Beispiel.

Als besonders kompliziert hat sich erwiesen, den Inhalt der hochgeladenen Datei zu bestimmen, da er als ein `Flux<DataBuffer>` zur Verfügung steht, also einen Stream aus Dateisegmenten, die wohl jeweils nur 1024 Bytes groß sind. Die Tatsache, dass es keinen komfortablen Zugriff auf diese Daten gibt, beweist wohl, dass _Reactive Programming_ eher ein Stiefkind in der Java-Welt ist. 

Ohne die Paradigmen an dieser Stelle vergleichen zu wollen ist anzunehmen, dass die seit Java 21 verfügbaren _virtuellen Threads_ für denselben Einsatz mehr Interesse wecken werden.

## Grundlagen für Reactive Programming

* [Project Reactor](https://projectreactor.io/)
* [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)

## Gerüst für Projekt

[Spring Initializr](https://start.spring.io/) legt auch HELP.md an.

## File Upload mit cURL

```
curl -X 'POST' \
'http://localhost:8080/analyze_image' \
-H 'accept: application/json' \
-H 'Content-Type: multipart/form-data' \
-F 'file=@/home/tom/Bilder/IMG_2336.JPG;type=image/jpeg'
```


## Swagger-UI

Eingebunden werden muss die Dependency

```
'org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0'
```

Die URL der UI lautet dann http://localhost:8080/swagger-ui.html und wird weitergeleitet zu http://localhost:8080/webjars/swagger-ui/index.html.
