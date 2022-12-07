package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;

/**
 * Service for vehicle data and operations.
 *
 * @author Sven Trachsel
 */
public interface VehicleDataService {

    /**
     * Gets the amount of vehicle data as GeoJSON.
     *
     * @return vehicle data
     */
    GeoJsonFeatureCollectionDto getCurrentVehicleData();

    /**
     * Gets the amount of vehicles in the past from a specific measurement-point as GeoJSON
     * @param measurementPointId the id of the selected measurement point
     * @param duration duration from now backwards what data should be selected (24h or 7d)
     * @return vehicle data in the past duration
     * @throws IllegalArgumentException when an unsupported duration is entered
     */
    HistoricDataCollectionDto getHistoricalVehicleData(String measurementPointId, String duration)  throws IllegalArgumentException;

    /**
     * Checks whether historic data for the measurement point is available
     * @param id the id of the measurement point
     * @return true if data is available, false if not
     */
	boolean hasHistoricData(String id);
}
