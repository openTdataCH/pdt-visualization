package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.config.SpeedDisplayConfig;
import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.mapper.DtoMapperImpl;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.VehicleAmount;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import ch.bfh.trafficcounter.repository.VehicleAmountRepository;
import ch.opentdata.wsdl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
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
public class VehicleAmountServiceImplTest {

	private final SpeedDisplayConfig speedDisplayConfig = new SpeedDisplayConfig();

	@Spy
	private DtoMapper dtoMapper = new DtoMapperImpl(speedDisplayConfig);

	@Mock
	private MeasurementRepository measurementRepository;

	@Mock
	private MeasurementPointRepository measurementPointRepository;

	@Mock
	private VehicleAmountRepository vehicleAmountRepository;

	private VehicleAmountServiceImpl vehicleAmountService;


	@BeforeEach
	void init() {
		final SpeedDisplayConfig.SpeedDisplayThresholds thresholds = new SpeedDisplayConfig.SpeedDisplayThresholds();
		thresholds.setHigh(0.9f);
		thresholds.setNeutral(0.65f);
		thresholds.setLow(0.0f);
		speedDisplayConfig.setThresholds(thresholds);
		this.vehicleAmountService = new VehicleAmountServiceImpl(
				measurementRepository,
				vehicleAmountRepository,
				measurementPointRepository,
				dtoMapper
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

		final Integer expectedAmountOfVehicles = firstSpeed.getFirst() + secondSpeed.getFirst();

		final VehicleAmount vehicleAmount = vehicleAmountService.processMeasurement(measurement, siteMeasurements);

		assertEquals(measurementPointId, vehicleAmount.getMeasurementPoint().getId());
		assertEquals(expectedAmountOfVehicles, vehicleAmount.getNumberOfVehicles());
	}

	@Test
	void testProcessAndPersistVehicleAmount_existingMeasurement() {
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

		vehicleAmountService.processAndPersistVehicleAmount(time, List.of(siteMeasurements));

		verify(vehicleAmountRepository, times(1)).saveAll(any());
	}

	@Test
	void testProcessAndPersistVehicleAmount_newMeasurement() {
		final LocalDateTime time = LocalDateTime.of(2022, 11, 11, 1, 1, 0, 0);
		final String measurementPointId = "ABC";
		final SiteMeasurements siteMeasurements = generateSiteMeasurements(measurementPointId, List.of(Pair.of(2, 50f)));

		when(measurementPointRepository.findById(measurementPointId))
				.thenReturn(Optional.of(MeasurementPoint.builder().id(measurementPointId).build()));

		when(measurementRepository.findByTime(time)).thenReturn(Optional.empty());

		vehicleAmountService.processAndPersistVehicleAmount(time, List.of(siteMeasurements));

		verify(measurementRepository, times(1)).save(any());
		verify(vehicleAmountRepository, times(1)).saveAll(any());
	}

	@Test
	void testGetCurrentVehicleAmount() {
		final LocalDateTime time = LocalDateTime.of(2022, 11, 11, 1, 1, 0, 0);
		final Measurement measurement = Measurement.builder()
				.id(1L)
				.time(time)
				.vehicleAmounts(Set.of(
						VehicleAmount.builder()
								.id(1L)
								.numberOfVehicles(3)
								.measurementPoint(MeasurementPoint.builder().id("ABC").build())
								.build()
				)).build();
		when(measurementRepository.findLatest()).thenReturn(Optional.of(measurement));
		final GeoJsonFeatureCollectionDto vehicleAmountGeoJson = vehicleAmountService.getCurrentVehicleAmount();
		assertEquals(1, vehicleAmountGeoJson.getFeatures().size());
		assertEquals(3, vehicleAmountGeoJson.getFeatures().get(0).getProperties().getVehicleAmount().getNumberOfVehicles());
	}

}
