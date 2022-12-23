package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.config.SpeedDisplayConfig;
import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.mapper.DtoMapperImpl;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MeasurementPointServiceTest {

    @Spy
    private DtoMapper dtoMapper = new DtoMapperImpl();

    @Mock
    private MeasurementPointRepository measurementPointRepository;

    private MeasurementPointService measurementPointService;

    @BeforeEach
    void init() {
        final SpeedDisplayConfig.SpeedDisplayThresholds thresholds = new SpeedDisplayConfig.SpeedDisplayThresholds();
        thresholds.setHigh(0.9f);
        thresholds.setNeutral(0.65f);
        thresholds.setLow(0.0f);
        this.measurementPointService = new MeasurementPointServiceImpl(dtoMapper, measurementPointRepository);
    }

    @Test
    void testGetAllMeasurementPointsGeoJson_active() {
        final ArrayList<MeasurementPoint> measurementPoints = new ArrayList<>(0);
        final MeasurementPoint measurementPoint = MeasurementPoint.builder()
            .id("TEST")
            .numberOfLanes(1)
            .longtitude(1)
            .latitude(1)
            .active(true)
            .build();
        measurementPoints.add(measurementPoint);
        when(measurementPointRepository.findAllByActive(true)).thenReturn(measurementPoints);

        final GeoJsonFeatureCollectionDto result = measurementPointService.getAllMeasurementPointsGeoJson();
        verify(dtoMapper, times(1)).mapMeasurementPointsToGeoJsonFeatureCollectionDto(any());
        assertEquals(1, result.getFeatures().size());
        assertEquals(measurementPoint.getId(), result.getFeatures().get(0).getProperties().getId());
        assertEquals(measurementPoint.getLongtitude(), result.getFeatures().get(0).getGeometry().getCoordinates()[0]);
        assertEquals(measurementPoint.getLatitude(), result.getFeatures().get(0).getGeometry().getCoordinates()[1]);
    }


    @Test
    void testGetNumberOfMeasurementPoints() {
        final int numActive = 10;
        when(measurementPointRepository.countAllByActive(true)).thenReturn(numActive);

        final int result = measurementPointService.getNumberOfMeasurementPoints();

        assertEquals(numActive, result);
    }

}
