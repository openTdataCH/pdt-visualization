package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.event.UpdateEvent;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.SpeedDataDto;
import ch.bfh.trafficcounter.service.SpeedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

/**
 * Controller for providing speed data.
 *
 * @author Manuel Riesen
 */
@RestController
@RequestMapping("/api/speeddata")
public class SpeedDataController {

    private final SpeedDataService speedDataService;

    private final Sinks.Many<UpdateEvent> updateEvent;

    @Autowired
    public SpeedDataController(SpeedDataService speedDataService, Sinks.Many<UpdateEvent> updateEvent) {
        this.speedDataService = speedDataService;
        this.updateEvent = updateEvent;
    }

    /**
     * Gets the current speed data as GeoJson.
     *
     * @return GeoJson including speed data
     */
    @GetMapping
    public ResponseEntity<GeoJsonFeatureCollectionDto> getCurrentSpeedData() {
        return ResponseEntity.ok(speedDataService.getCurrentSpeedData());
    }

    /**
     * Gets the current speed data as GeoJson in a reactive way.
     * The speed data can be consumed by SSE.
     *
     * @return GeoJson including speed data
     */
    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<GeoJsonFeatureCollectionDto>> getCurrentSpeedDataReactive() {
        return updateEvent.asFlux().map(event -> ServerSentEvent.<GeoJsonFeatureCollectionDto>builder()
                .data(speedDataService.getCurrentSpeedData())
                .event("message")
                .build()
        );
    }
}
