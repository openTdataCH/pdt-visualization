package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.dto.geojson.VehicleAmountDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.VehicleAmount;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import ch.bfh.trafficcounter.repository.VehicleAmountRepository;
import ch.opentdata.wsdl.SiteMeasurements;
import ch.opentdata.wsdl.TrafficSpeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link VehicleAmountService}.
 *
 * @author Sven Trachsel
 */
@Service
@Transactional
public class VehicleAmountServiceImpl implements VehicleAmountService {


    private final MeasurementRepository measurementRepository;

    private final VehicleAmountRepository vehicleAmountRepository;

    private final MeasurementPointRepository measurementPointRepository;

    private final DtoMapper dtoMapper;

    @Autowired
    public VehicleAmountServiceImpl(MeasurementRepository measurementRepository, VehicleAmountRepository vehicleAmountRepository, MeasurementPointRepository measurementPointRepository, DtoMapper dtoMapper) {
        this.measurementRepository = measurementRepository;
        this.vehicleAmountRepository = vehicleAmountRepository;
        this.measurementPointRepository = measurementPointRepository;
        this.dtoMapper = dtoMapper;
    }

    public VehicleAmount processMeasurement(final Measurement measurement, final SiteMeasurements siteMeasurements) {

        final String measurementPointId = siteMeasurements.getMeasurementSiteReference().getId();
        final MeasurementPoint measurementPoint = measurementPointRepository.findById(measurementPointId).orElseThrow();

        final Integer numberOfVehicles = siteMeasurements.getMeasuredValue().stream()
            .map(value -> value.getMeasuredValue().getBasicData())
            .filter(value -> value instanceof TrafficSpeed)
            .mapToInt(value -> ((TrafficSpeed) value).getAverageVehicleSpeed().getNumberOfInputValuesUsed().intValue())
            .sum();
        return VehicleAmount.builder()
            .measurement(measurement)
            .measurementPoint(measurementPoint)
            .numberOfVehicles(numberOfVehicles)
            .build();
    }

    @Override
    public void processAndPersistVehicleAmount(LocalDateTime time, List<SiteMeasurements> siteMeasurements) {
        final Measurement measurement = measurementRepository.findByTime(time)
            .orElseGet(() -> measurementRepository.save(Measurement.builder().time(time).build()));

        final List<VehicleAmount> vehicleAmounts = siteMeasurements.parallelStream()
            .map(siteMeasurement -> processMeasurement(measurement, siteMeasurement))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        vehicleAmountRepository.saveAll(vehicleAmounts);
    }

    @Override
    public List<GeoJsonFeatureDto> getVehicleAmount(final Measurement measurement) {
        return measurement.getVehicleAmounts().stream()
            .map(this::transformToGeoJsonFeatureDto)
            .collect(Collectors.toList());
    }

    @Override
    public boolean hasAmountData() {
        return vehicleAmountRepository.existsByIdIsGreaterThan(0l);
    }

    private GeoJsonFeatureDto transformToGeoJsonFeatureDto(final VehicleAmount vehicleAmount) {
        final GeoJsonFeatureDto geoJsonFeatureDto = dtoMapper.mapMeasurementPointToGeoJsonFeatureDto(vehicleAmount.getMeasurementPoint());
        final VehicleAmountDto vehicleAmountDto = new VehicleAmountDto(vehicleAmount.getNumberOfVehicles());
        geoJsonFeatureDto.getProperties().setVehicleAmount(vehicleAmountDto);
        return geoJsonFeatureDto;
    }
}
