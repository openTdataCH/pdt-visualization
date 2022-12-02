package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import ch.bfh.trafficcounter.repository.SpeedDataRepository;
import ch.bfh.trafficcounter.repository.VehicleAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;


/**
 * Implementation of {@link VehicleAmountService}.
 *
 * @author Sven Trachsel
 */
@Service
@Transactional
public class VehicleDataServiceImpl implements VehicleDataService {


    private final MeasurementRepository measurementRepository;

    private final VehicleAmountRepository vehicleAmountRepository;

    private final SpeedDataRepository speedDataRepository;

    private final MeasurementPointRepository measurementPointRepository;

    private final DtoMapper dtoMapper;

    @Autowired
    public VehicleDataServiceImpl(MeasurementRepository measurementRepository, VehicleAmountRepository vehicleAmountRepository, SpeedDataRepository speedDataRepository, MeasurementPointRepository measurementPointRepository, DtoMapper dtoMapper) {
        this.measurementRepository = measurementRepository;
        this.vehicleAmountRepository = vehicleAmountRepository;
        this.speedDataRepository = speedDataRepository;
        this.measurementPointRepository = measurementPointRepository;
        this.dtoMapper = dtoMapper;
    }

    private Optional<Measurement> getLatestMeasurement() {
        return measurementRepository.findLatest()
            .stream().findFirst();
    }

    @Override
    public GeoJsonFeatureCollectionDto getCurrentVehicleData() {
        Optional<Measurement> latestMeasurement = getLatestMeasurement();

        return dtoMapper.mapVehicleDataToGeoJsonFeatureCollectionDto(
            latestMeasurement
                .map(Measurement::getVehicleAmounts)
                .orElse(Collections.emptySet()),
            latestMeasurement
                .map(Measurement::getSpeedData)
                .orElse(Collections.emptySet())
        );
    }
}
