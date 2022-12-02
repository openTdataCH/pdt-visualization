package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;

/**
 * Service for vehicle-amount data and operations.
 *
 * @author Sven Trachsel
 */
public interface VehicleDataService {

    /**
     * Gets the amount of vehicles data as GeoJSON.
     *
     * @return amount of vehicles
     */
    GeoJsonFeatureCollectionDto getCurrentVehicleData();

}
