package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.service.MeasurementPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/measurementpoints")
public class MeasurementPointController {

	private final MeasurementPointService measurementPointService;

	public MeasurementPointController(MeasurementPointService measurementPointService) {
		this.measurementPointService = measurementPointService;
	}

	@GetMapping
	public ResponseEntity<GeoJsonFeatureCollectionDto> getMeasurementPointsGeoJson() {
		return ResponseEntity.ok(measurementPointService.getAllMeasurementPointsGeoJson());
	}


}

