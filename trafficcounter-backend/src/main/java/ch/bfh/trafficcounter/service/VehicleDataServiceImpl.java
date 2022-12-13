package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.HistoricMeasurement;
import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import ch.bfh.trafficcounter.repository.SpeedDataRepository;
import ch.bfh.trafficcounter.repository.VehicleAmountRepository;
import org.glassfish.pfl.basic.contain.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public HistoricDataCollectionDto getHistoricalVehicleData(String measurementPointId, String duration) {

        LocalDateTime now = LocalDateTime.now();
        ArrayList<Pair<LocalDateTime, LocalDateTime>> timeSpans = new ArrayList<>();
        ArrayList<HistoricMeasurement> historicMeasurements = new ArrayList<>();

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
            /*
            Future<Double> avgSpeedFt = runSumSpeedQuery(measurementPointId, ts.second(), ts.first());
            Future<Integer> avgAmountFt = runSumAmountQuery(measurementPointId, ts.second(), ts.first());
            Double avgSpeed;
            Integer avgAmount;

            try {
                avgSpeed = avgSpeedFt.get();
                avgAmount = avgAmountFt.get();
            } catch (CancellationException | ExecutionException | InterruptedException e) {
                historicMeasurements.add(new HistoricMeasurement(
                    ordinal,
                    ts.first(),
                    0,
                    0
                ));
                System.out.println("Unable to get result from async task");
                continue;
            }
             */

            Double avgSpeed = measurementRepository.findAverageVehicleSpeedByTimeBetweenAndMeasurementPointId(measurementPointId, ts.second(), ts.first());
            Integer avgAmount = measurementRepository.findSumVehicleAmountByTimeBetweenAndMeasurementPointId(measurementPointId, ts.second(), ts.first());

            if (avgSpeed == null) {
                avgSpeed = 0d;
            }
            if (avgAmount == null) {
                avgAmount = 0;
            }

            historicMeasurements.add(new HistoricMeasurement(
                ordinal,
                ts.first(),
                avgSpeed,
                avgAmount
            ));
            ordinal++;
        }

        return dtoMapper.mapHistoricVehicleDataToHistoricDataDto(historicMeasurements, duration.substring(duration.length() - 1));
    }

    @Override
    public boolean hasHistoricData(String id) {
        return measurementPointRepository.existsMeasurementPointById(id);
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
