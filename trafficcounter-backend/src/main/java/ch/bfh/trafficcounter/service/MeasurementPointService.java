package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;


/**
 * Service for Measurement points
 *
 * @author Sven Trachsel
 */
public interface MeasurementPointService {

    /**
     * Gets a GeoJSON-List of all registered Measurement Points
     * Only finds active measurement points
     * @return a DTO in GeoJSON format
     */
    GeoJsonFeatureCollectionDto getAllMeasurementPointsGeoJson();


    /**
     * Counts number of active measurement points, zero indicates that no data has been loaded yet
     * @return number of active measurement points
     */
    int getNumberOfMeasurementPoints();
}
