package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.HistoricMeasurement;
import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.model.entity.SpeedData;
import ch.bfh.trafficcounter.model.entity.VehicleAmount;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import org.glassfish.pfl.basic.contain.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Implementation of {@link VehicleAmountService}.
 *
 * @author Sven Trachsel
 */
@Service
@Transactional
public class VehicleDataServiceImpl implements VehicleDataService {


    private final MeasurementRepository measurementRepository;

    private final MeasurementPointRepository measurementPointRepository;

    private final DtoMapper dtoMapper;

    private final VehicleAmountService vehicleAmountService;

    private final SpeedDataService speedDataService;

    @Autowired
    public VehicleDataServiceImpl(
        MeasurementRepository measurementRepository,
        MeasurementPointRepository measurementPointRepository,
        DtoMapper dtoMapper,
        VehicleAmountService vehicleAmountService,
        SpeedDataService speedDataService
    ) {
        this.measurementRepository = measurementRepository;
        this.measurementPointRepository = measurementPointRepository;
        this.dtoMapper = dtoMapper;
        this.vehicleAmountService = vehicleAmountService;
        this.speedDataService = speedDataService;
    }


    private Optional<Measurement> getLatestMeasurement() {
        return measurementRepository.findLatest()
            .stream().findFirst();
    }

    @Override
    public GeoJsonFeatureCollectionDto getCurrentVehicleData() {
        Optional<Measurement> latestMeasurement = getLatestMeasurement();

        final List<GeoJsonFeatureDto> geoJsonFeatureDtos = latestMeasurement.map(measurement -> Stream.concat(
                speedDataService.getSpeedData(measurement).stream(),
                vehicleAmountService.getVehicleAmount(measurement).stream()
            ).collect(Collectors.toMap(feature -> feature.getProperties().getId(), Function.identity(), (feature1, feature2) -> {
                    if (feature1.getProperties().getSpeedData() == null) {
                        feature1.getProperties().setSpeedData(feature2.getProperties().getSpeedData());
                    }
                    if (feature1.getProperties().getVehicleAmount() == null) {
                        feature1.getProperties().setVehicleAmount(feature2.getProperties().getVehicleAmount());
                    }
                    return feature1;
                })
            ).values().stream().toList())
            .orElse(Collections.emptyList());

        return new GeoJsonFeatureCollectionDto(geoJsonFeatureDtos);
    }

    @Override
    public HistoricDataCollectionDto getHistoricalVehicleData(String measurementPointId, String duration) {

        LocalDateTime now = LocalDateTime.now();
        ArrayList<Pair<LocalDateTime, LocalDateTime>> timeSpans = new ArrayList<>();
        ArrayList<HistoricMeasurement> historicMeasurements = new ArrayList<>();
        ArrayList<Future<HistoricMeasurement>> futureHistoricMeasurements = new ArrayList<>();


        switch (duration) {
            case "24h":
                for (long i = 1L; i <= 24L; i++) {
                    timeSpans.add(new Pair<>(now.minusHours(i - 1), now.minusHours(i)));
                }
                break;
            case "7d":
                for (long i = 1L; i <= 7L; i++) {
                    timeSpans.add(new Pair<>(now.minusDays(i - 1), now.minusDays(i)));
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported duration: %s", duration));
        }

        // gets data either hourly 24x or daily 7x
        int ordinal = 1;
        for (Pair<LocalDateTime, LocalDateTime> ts : timeSpans) {
            futureHistoricMeasurements.add(buildHistoricMeasurementPart(
                ts.second(),
                ts.first(),
                ordinal,
                measurementPointId
            ));
            ordinal++;
        }

        for (Future<HistoricMeasurement> fhm : futureHistoricMeasurements) {
            try {
                historicMeasurements.add(fhm.get());
            } catch (CancellationException | InterruptedException | ExecutionException e) {
                historicMeasurements.add(new HistoricMeasurement(0, LocalDateTime.now(), 0, 0));
            }
        }

        return dtoMapper.mapHistoricVehicleDataToHistoricDataDto(historicMeasurements, duration.substring(duration.length() - 1));
    }

    @Override
    public boolean hasHistoricData(String id) {
        return measurementPointRepository.existsMeasurementPointById(id);
    }

    @Async
    public Future<HistoricMeasurement> buildHistoricMeasurementPart(LocalDateTime start, LocalDateTime end, int ordinal, String measurementPointId) {
        Double avgSpeed = measurementRepository.findAverageVehicleSpeedByTimeBetweenAndMeasurementPointId(measurementPointId, start, end);
        Integer avgAmount = measurementRepository.findSumVehicleAmountByTimeBetweenAndMeasurementPointId(measurementPointId, start, end);

        if (avgSpeed == null) {
            avgSpeed = 0d;
        }
        if (avgAmount == null) {
            avgAmount = 0;
        }

        return new AsyncResult<>(
            new HistoricMeasurement(
                ordinal,
                end,
                avgSpeed,
                avgAmount
            ));
    }


    /*
    @Async
    public Future<Double> runSumSpeedQuery(String measurementPointId, LocalDateTime start, LocalDateTime end) {
        Double avgSpeed = measurementRepository.findAverageVehicleSpeedByTimeBetweenAndMeasurementPointId(measurementPointId, start, end);
        return new AsyncResult<>(avgSpeed);
    }

    @Async
    public Future<Integer> runSumAmountQuery(String measurementPointId, LocalDateTime start, LocalDateTime end) {
        Integer sumAmount = measurementRepository.findSumVehicleAmountByTimeBetweenAndMeasurementPointId(measurementPointId, start, end);
        return new AsyncResult<>(sumAmount);
    }
     */
}
