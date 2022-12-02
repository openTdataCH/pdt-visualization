package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.event.UpdateEvent;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.service.VehicleAmountService;
import ch.bfh.trafficcounter.service.VehicleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

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
	public Flux<GeoJsonFeatureCollectionDto> getCurrentVehicleDataReactive() {
		return updateEvent.asFlux().map(event -> vehicleDataService.getCurrentVehicleData());
	}

}
