package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.event.UpdateEvent;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.service.VehicleAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Controller for providing vehicle amount data.
 *
 * @author Sven Trachsel
 */
@RestController
@RequestMapping("/api/vehicleamount")
public class VehicleAmountController {

	private final VehicleAmountService vehicleAmountService;

	private final Sinks.Many<UpdateEvent> updateEvent;

	@Autowired
	public VehicleAmountController(VehicleAmountService vehicleAmountService, Sinks.Many<UpdateEvent> updateEvent) {
		this.vehicleAmountService = vehicleAmountService;
		this.updateEvent = updateEvent;
	}

	/**
	 * Gets the current amount of vehicles as GeoJson.
	 *
	 * @return GeoJson including amount of vehicles
	 */
	@GetMapping
	public ResponseEntity<GeoJsonFeatureCollectionDto> getCurrentAmountOfVehicles() {
		final var entity = ResponseEntity.ok(vehicleAmountService.getCurrentVehicleAmount());
		return entity;
	}

	/**
	 * Gets the current amount of vehicles as GeoJson in a reactive way.
	 * The speed data can be consumed by SSE.
	 *
	 * @return GeoJson including amount of vehicles
	 */
	@GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<GeoJsonFeatureCollectionDto>> getCurrentAmountOfVehiclesReactive() {
		return updateEvent.asFlux().map(event -> ServerSentEvent.<GeoJsonFeatureCollectionDto>builder()
				.data(vehicleAmountService.getCurrentVehicleAmount())
				.event("message")
				.build()
		);
	}

}
