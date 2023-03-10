/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.opentdata.wsdl.SiteMeasurements;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for speed-related data and operations.
 *
 * @author Manuel Riesen
 */
public interface SpeedDataService {

    /**
     * Processes and persists all speed data
     *
     * @param time             measurement time
     * @param siteMeasurements site measurements
     */
    void processAndPersistSpeedData(LocalDateTime time, List<SiteMeasurements> siteMeasurements);

    /**
     * Gets the current speed data as GeoJSON.
     *
     * @param measurement measurement to get speed data from
     * @return current speed data
     */
    List<GeoJsonFeatureDto> getSpeedData(Measurement measurement);

    /**
     * Updates the estimated speed limit
     */
    void updateEstimatedSpeedLimit();

    /**
     * Checks whether SpeedData is present
     *
     * @return true if speed data is present, false if not
     */
    boolean hasSpeedData();

}
