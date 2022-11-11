package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.dto.geojson.SpeedDataDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.SpeedData;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import ch.bfh.trafficcounter.repository.SpeedDataRepository;
import ch.opentdata.wsdl.SiteMeasurements;
import ch.opentdata.wsdl.TrafficSpeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpeedDataServiceImpl implements SpeedDataService {

    private final MeasurementRepository measurementRepository;

    private final SpeedDataRepository speedDataRepository;

    private final MeasurementPointRepository measurementPointRepository;

    private final DtoMapper dtoMapper;

    @Autowired
    public SpeedDataServiceImpl(
            MeasurementRepository measurementRepository,
            SpeedDataRepository speedDataRepository,
            MeasurementPointRepository measurementPointRepository,
            DtoMapper dtoMapper
    ) {
        this.measurementRepository = measurementRepository;
        this.speedDataRepository = speedDataRepository;
        this.measurementPointRepository = measurementPointRepository;
        this.dtoMapper = dtoMapper;
    }

    private record TrafficSpeedAggregation(int numberOfInputValuesUsed, float speedProduct) {

        public TrafficSpeedAggregation sum(final TrafficSpeedAggregation other) {
            return new TrafficSpeedAggregation(numberOfInputValuesUsed + other.numberOfInputValuesUsed, speedProduct + other.speedProduct);
        }

        public Float getAverageSpeed() {
            if (numberOfInputValuesUsed == 0) {
                return null;
            }
            return speedProduct / numberOfInputValuesUsed;
        }
    }

    public SpeedData processMeasurement(final Measurement measurement, final SiteMeasurements siteMeasurements) {
        final String measurementPointId = siteMeasurements.getMeasurementSiteReference().getId();
        final MeasurementPoint measurementPoint = measurementPointRepository.findById(measurementPointId).orElseThrow();
        final Float averageSpeedValue = siteMeasurements.getMeasuredValue().stream()
                .map(value -> value.getMeasuredValue().getBasicData())
                .filter(value -> value instanceof TrafficSpeed)
                .map(value -> ((TrafficSpeed) value).getAverageVehicleSpeed())
                .filter(value -> value.getNumberOfInputValuesUsed().intValue() > 0)
                .map(value -> new TrafficSpeedAggregation(
                        value.getNumberOfInputValuesUsed().intValue(),
                        value.getSpeed() * value.getNumberOfInputValuesUsed().intValue())
                )
                .reduce(new TrafficSpeedAggregation(0, 0f), TrafficSpeedAggregation::sum)
                .getAverageSpeed();
        return SpeedData.builder()
                .measurement(measurement)
                .measurementPoint(measurementPoint)
                .averageSpeed(averageSpeedValue)
                .build();
    }

    @Override
    public void processAndPersistSpeedData(final LocalDateTime time, final List<SiteMeasurements> siteMeasurements) {
        final Measurement measurement = measurementRepository.findByTime(time)
                .orElseGet(() -> measurementRepository.save(Measurement.builder().time(time).build()));

        final List<SpeedData> speedData = siteMeasurements.parallelStream()
                .map(siteMeasurement -> processMeasurement(measurement, siteMeasurement))
                .filter(value -> value.getAverageSpeed() != null)
                .collect(Collectors.toList());
        speedDataRepository.saveAll(speedData);
    }

    private Optional<Measurement> getLatestMeasurement() {
        return measurementRepository.findTimeDesc(Pageable.ofSize(1))
                .stream().findFirst();
    }

    @Override
    public List<SpeedDataDto> getCurrentSpeedData() {
        return dtoMapper.mapSpeedDataToSpeedDataDto(
                getLatestMeasurement()
                        .map(Measurement::getSpeedData)
                        .orElse(Collections.emptySet())
        );
    }
}
