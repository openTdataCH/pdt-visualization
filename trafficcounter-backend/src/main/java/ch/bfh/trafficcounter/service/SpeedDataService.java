package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
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
     * @param time measurement time
     * @param siteMeasurements site measurements
     */
    void processAndPersistSpeedData(LocalDateTime time, List<SiteMeasurements> siteMeasurements);

    /**
     * Gets the current speed data as GeoJSON.
     * @return current speed data
     */
    GeoJsonFeatureCollectionDto getCurrentSpeedData();

}
