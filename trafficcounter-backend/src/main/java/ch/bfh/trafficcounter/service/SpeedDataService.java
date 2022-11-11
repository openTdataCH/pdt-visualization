package ch.bfh.trafficcounter.service;

import ch.opentdata.wsdl.SiteMeasurements;

import java.time.LocalDateTime;
import java.util.List;

public interface SpeedDataService {

    /**
     * Processes and persists all speed data
     * @param time measurement time
     * @param siteMeasurements site measurements
     */
    void processAndPersistSpeedData(LocalDateTime time, List<SiteMeasurements> siteMeasurements);
}
