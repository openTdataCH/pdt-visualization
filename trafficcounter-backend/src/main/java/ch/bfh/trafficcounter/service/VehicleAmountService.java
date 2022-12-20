package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.opentdata.wsdl.SiteMeasurements;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for vehicle-amount data and operations.
 *
 * @author Sven Trachsel
 */
public interface VehicleAmountService {

    /**
     * Processes and persists amount of vehicles
     *
     * @param time             measurement time
     * @param siteMeasurements site measurements
     */
    void processAndPersistVehicleAmount(LocalDateTime time, List<SiteMeasurements> siteMeasurements);

    /**
     * Gets the amount of vehicles data as GeoJSON.
     *
     * @param measurement measurement to get vehicle amount from
     * @return amount of vehicles
     */
    List<GeoJsonFeatureDto> getVehicleAmount(Measurement measurement);

    /**
     * Checks whether AmountData is present
     *
     * @return true if amount data is present, false if not
     */
    boolean hasAmountData();

}
