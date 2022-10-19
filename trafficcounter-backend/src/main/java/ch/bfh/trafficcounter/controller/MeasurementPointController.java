package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.service.MeasurementPointService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/measurementpoints")
public class MeasurementPointController {

	private final MeasurementPointService measurementPointService;

	public MeasurementPointController(MeasurementPointService measurementPointService) {
		this.measurementPointService = measurementPointService;
	}

	@GetMapping
	public ResponseEntity<GeoJsonFeatureCollectionDto> getMeasurementPointsGeoJson() {
		if (measurementPointService.getNumberOfMeasurementPoints() == 0) {
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
		return ResponseEntity.ok(measurementPointService.getAllMeasurementPointsGeoJson());
	}


}

