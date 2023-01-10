/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.config.SpeedDisplayConfig;
import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.mapper.DtoMapperImpl;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.SpeedData;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import ch.bfh.trafficcounter.repository.SpeedDataRepository;
import ch.opentdata.wsdl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpeedDataServiceImplTest {

    private final SpeedDisplayConfig speedDisplayConfig = new SpeedDisplayConfig();

    @Spy
    private DtoMapper dtoMapper = new DtoMapperImpl();

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private MeasurementPointRepository measurementPointRepository;

    @Mock
    private SpeedDataRepository speedDataRepository;


    private SpeedDataServiceImpl speedDataService;


    @BeforeEach
    void init() {
        final SpeedDisplayConfig.SpeedDisplayThresholds thresholds = new SpeedDisplayConfig.SpeedDisplayThresholds();
        thresholds.setHigh(0.9f);
        thresholds.setNeutral(0.65f);
        thresholds.setLow(0.0f);
        speedDisplayConfig.setThresholds(thresholds);
        this.speedDataService = new SpeedDataServiceImpl(
            measurementRepository,
            speedDataRepository,
            measurementPointRepository,
            dtoMapper,
            speedDisplayConfig
        );
    }

    private SiteMeasurements generateSiteMeasurements(final String measurementPointId, final List<Pair<Integer, Float>> speedDataValues) {
        return new SiteMeasurements() {{
            measurementSiteReference = new MeasurementSiteRecordVersionedReference();
            measurementSiteReference.setId(measurementPointId);
            measuredValue = speedDataValues.stream().map(speedDataValue -> {
                final SiteMeasurementsIndexMeasuredValue siteMeasurementsIndexMeasuredValue = new SiteMeasurementsIndexMeasuredValue();
                siteMeasurementsIndexMeasuredValue.setIndex(1);
                final MeasuredValue value = new MeasuredValue();
                final TrafficSpeed trafficSpeed = new TrafficSpeed();
                final SpeedValue speedValue = new SpeedValue();
                speedValue.setNumberOfInputValuesUsed(BigInteger.valueOf(speedDataValue.getFirst()));
                speedValue.setSpeed(speedDataValue.getSecond());
                trafficSpeed.setAverageVehicleSpeed(speedValue);
                value.setBasicData(trafficSpeed);
                siteMeasurementsIndexMeasuredValue.setMeasuredValue(value);
                return siteMeasurementsIndexMeasuredValue;
            }).collect(Collectors.toList());
        }};
    }

    @Test
    void testProcessMeasurement() {
        final Measurement measurement = Measurement.builder()
            .id(1L)
            .time(LocalDateTime.of(2022, 11, 11, 1, 1, 0, 0))
            .build();
        final Pair<Integer, Float> firstSpeed = Pair.of(2, 50f);
        final Pair<Integer, Float> secondSpeed = Pair.of(1, 100f);
        final String measurementPointId = "ABC";
        final SiteMeasurements siteMeasurements = generateSiteMeasurements(measurementPointId, List.of(firstSpeed, secondSpeed));

        when(measurementPointRepository.findById(measurementPointId))
            .thenReturn(Optional.of(MeasurementPoint.builder().id(measurementPointId).build()));

        final SpeedData speedData = speedDataService.processMeasurement(measurement, siteMeasurements);

        final float expectedAverageSpeed = (
            firstSpeed.getFirst() * firstSpeed.getSecond()
                + secondSpeed.getFirst() * secondSpeed.getSecond()
        ) / (firstSpeed.getFirst() + secondSpeed.getFirst());

        assertEquals(measurementPointId, speedData.getMeasurementPoint().getId());
        assertEquals(expectedAverageSpeed, speedData.getAverageSpeed());
    }

    @Test
    void testProcessAndPersistSpeedData_existingMeasurement() {
        final LocalDateTime time = LocalDateTime.of(2022, 11, 11, 1, 1, 0, 0);
        final Measurement measurement = Measurement.builder()
            .id(1L)
            .time(time)
            .build();
        final String measurementPointId = "ABC";
        final SiteMeasurements siteMeasurements = generateSiteMeasurements(measurementPointId, List.of(Pair.of(2, 50f)));

        when(measurementPointRepository.findById(measurementPointId))
            .thenReturn(Optional.of(MeasurementPoint.builder().id(measurementPointId).build()));

        when(measurementRepository.findByTime(time)).thenReturn(Optional.of(measurement));

        speedDataService.processAndPersistSpeedData(time, List.of(siteMeasurements));

        verify(speedDataRepository, times(1)).saveAll(any());
    }

    @Test
    void testProcessAndPersistSpeedData_newMeasurement() {
        final LocalDateTime time = LocalDateTime.of(2022, 11, 11, 1, 1, 0, 0);
        final String measurementPointId = "ABC";
        final SiteMeasurements siteMeasurements = generateSiteMeasurements(measurementPointId, List.of(Pair.of(2, 50f)));

        when(measurementPointRepository.findById(measurementPointId))
            .thenReturn(Optional.of(MeasurementPoint.builder().id(measurementPointId).build()));

        when(measurementRepository.findByTime(time)).thenReturn(Optional.empty());

        speedDataService.processAndPersistSpeedData(time, List.of(siteMeasurements));

        verify(measurementRepository, times(1)).save(any());
        verify(speedDataRepository, times(1)).saveAll(any());
    }

    @Test
    void testGetCurrentSpeedData() {
        final LocalDateTime time = LocalDateTime.of(2022, 11, 11, 1, 1, 0, 0);
        final Measurement measurement = Measurement.builder()
            .id(1L)
            .time(time)
            .speedData(Set.of(
                SpeedData.builder()
                    .id(1L)
                    .averageSpeed(10f)
                    .measurementPoint(MeasurementPoint.builder().id("ABC").build())
                    .build()
            )).build();
        final List<GeoJsonFeatureDto> speedDataGeoJson = speedDataService.getSpeedData(measurement);
        assertEquals(1, speedDataGeoJson.size());
        assertEquals(10f, speedDataGeoJson.get(0).getProperties().getSpeedData().getAverageSpeed());
    }
}
