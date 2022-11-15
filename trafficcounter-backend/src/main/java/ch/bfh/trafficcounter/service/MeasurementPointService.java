package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.opentdata.wsdl.MeasurementSiteRecord;

import java.util.List;


/**
 * Service for measurement points.
 *
 * @author Sven Trachsel
 */
public interface MeasurementPointService {

    /**
     * Processes and persists all measurement points.
     * @param measurementSiteRecords measurement site records
     */
    void processAndPersistMeasurementPoints(List<MeasurementSiteRecord> measurementSiteRecords);

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
