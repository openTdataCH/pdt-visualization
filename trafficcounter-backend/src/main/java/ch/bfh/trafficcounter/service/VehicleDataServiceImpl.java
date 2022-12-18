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

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Future;


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
        getAllHistoricalVehicleData(duration, LocalDateTime.now());
        //TODO, get data from cache table
        /*
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
                historicMeasurements.add(new HistoricMeasurement("", 0, LocalDateTime.now(), 0, 0));
            }
        }

        return historicMeasurements;
         */
        //return dtoMapper.mapHistoricVehicleDataToHistoricDataDto(historicMeasurements, duration.substring(duration.length() - 1));
        return null;
    }

    /**
     * Aggregates a list of historic measurement for a given duration
     * @param duration duration to aggregate over (24h or 7d)
     * @param now the start time from where results are aggregated backwards
     * @return a map where the key is the start time of the HistoricData and the value is a List of all aggregation for every MeasurementPoint in the database
     * has 24 entries for 24h and 7 entries for 7d
     */
    public HashMap<LocalDateTime, ArrayList<HistoricMeasurement>> getAllHistoricalVehicleData(String duration, LocalDateTime now) {

        ArrayList<Pair<LocalDateTime, LocalDateTime>> timeSpans = new ArrayList<>();
        HashMap<LocalDateTime, ArrayList<HistoricMeasurement>> historicMeasurements = new HashMap<>();
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
        for (Pair<LocalDateTime, LocalDateTime> ts : timeSpans) {
            historicMeasurements.put(ts.second(),
                buildHistoricMeasurementPart(
                    ts.second(),
                    ts.first()
                )
            );
        }

        return historicMeasurements;
    }

    @Override
    public boolean hasHistoricData(String id) {
        return measurementPointRepository.existsMeasurementPointById(id);
    }

    /**
     * Builds a list of all historic measurements in a given timespan. reads avg speed an amount data
     *
     * @param start start time for query
     * @param end   end time for query
     * @return List of HistoricMeasurement for every MeasurementPoint in the database in the requested timespan
     */
    public ArrayList<HistoricMeasurement> buildHistoricMeasurementPart(LocalDateTime start, LocalDateTime end) {

        ArrayList<HistoricMeasurement> historicMeasurements = new ArrayList<>();

        ArrayList<Tuple> avgSpeedList = measurementRepository.findAverageVehicleSpeedByTimeBetween(start, end);
        ArrayList<Tuple> avgAmountList = measurementRepository.findSumVehicleAmountByTimeBetween(start, end);

        for (Tuple ta : avgAmountList) {
            String taId = (String) ta.get(0);
            Integer amount = (Integer) ta.get(1);

            Optional<Tuple> optionalSpeedTuple = avgSpeedList.stream().filter(s -> s.get(0).equals(taId)).findFirst();
            Double speed = optionalSpeedTuple.isPresent() ? (Double) optionalSpeedTuple.get().get(1) : 0d;

            historicMeasurements.add(
                new HistoricMeasurement(
                    taId, start, speed, amount
                )
            );
        }

        return historicMeasurements;
    }

    //TODO, Schedule houry, best at exact hour XX:00
    private void aggregateVehicleDataHourly() {
        HashMap<LocalDateTime, ArrayList<HistoricMeasurement>> hourlyHistoricData = getAllHistoricalVehicleData("24h", LocalDateTime.now());

        //TODO, persist data

    }

    //TODO, Schedule daily, best at begin of day 00:00
    private void aggregateVehicleDataDaily() {
        HashMap<LocalDateTime, ArrayList<HistoricMeasurement>> dailyHistoricData = getAllHistoricalVehicleData("7d", LocalDateTime.now());

        //TODO, persist data
    }

}
