package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.event.UpdateEvent;
import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.service.VehicleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

/**
 * Controller for combined vehicle data
 *
 * @author Sven Trachsel
 */
@RestController
@RequestMapping("/api/vehicledata")
public class VehicleDataController {

    private final VehicleDataService vehicleDataService;

    private final Sinks.Many<UpdateEvent> updateEvent;

    @Autowired
    public VehicleDataController(VehicleDataService vehicleDataService, Sinks.Many<UpdateEvent> updateEvent) {
        this.vehicleDataService = vehicleDataService;
        this.updateEvent = updateEvent;
    }

    /**
     * Gets the current amount of vehicles and their speed as GeoJson.
     *
     * @return GeoJson including amount of vehicles
     */
    @GetMapping
    public ResponseEntity<GeoJsonFeatureCollectionDto> getCurrentVehicleData() {
        return ResponseEntity.ok(vehicleDataService.getCurrentVehicleData());
    }

    /**
     * Gets the current amount of vehicles and their speed as GeoJson in a reactive way.
     * The speed data can be consumed by SSE.
     *
     * @return GeoJson including amount of vehicles
     */
    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<GeoJsonFeatureCollectionDto>> getCurrentVehicleDataReactive() {
        final Mono<GeoJsonFeatureCollectionDto> blockingWrapper = Mono.fromCallable(vehicleDataService::getCurrentVehicleData);
        return updateEvent.asFlux()
            .map(event -> blockingWrapper.subscribeOn(Schedulers.boundedElastic()).flux())
            .flatMap(flux -> flux.map(vehicleData -> ServerSentEvent.<GeoJsonFeatureCollectionDto>builder()
                    .data(vehicleData)
                    .event("message")
                    .build()
                )
            );
    }

    /**
     * Gets the historic data for the given measurementpoint for the given duration.
     * @param id id of the measurementpoint
     * @param duration duration wanted (implies resolution 24h -> hourly, 7d -> daily)
     * @return a Json with historic data
     */
    @GetMapping("/api/vehicledata/history/{id}")
    public ResponseEntity<HistoricDataCollectionDto> getHistoricalVehicleData(@PathVariable("id") String id, @RequestParam("duration") String duration) {
        return ResponseEntity.ok(vehicleDataService.getHistoricalVehicleData(id, duration));
    }


    }
