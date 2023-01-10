/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.service.MeasurementPointService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Endpoint /api/measurementpoints
 *
 * @author Sven Trachsel
 */
@RestController
@RequestMapping("/api/measurementpoints")
public class MeasurementPointController {

    private final MeasurementPointService measurementPointService;

    public MeasurementPointController(MeasurementPointService measurementPointService) {
        this.measurementPointService = measurementPointService;
    }

    /**
     * Checks if data is present, if data is present, sends data
     * if not, send 503, service unavailable
     *
     * @return ResponseEntity with either data (and 200 ok) or 503 Service unavailable Status code
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoJsonFeatureCollectionDto> getMeasurementPointsGeoJson() {
        if (measurementPointService.getNumberOfMeasurementPoints() == 0) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
        return ResponseEntity.ok(measurementPointService.getAllMeasurementPointsGeoJson());
    }


}

