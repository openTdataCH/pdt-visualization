package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.model.entity.MeasurementStats;
import ch.bfh.trafficcounter.model.entity.MeasurementStatsType;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import org.glassfish.pfl.basic.contain.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.*;
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

    private final VehicleAmountService vehicleAmountService;

    private final SpeedDataService speedDataService;

    @Autowired
    public VehicleDataServiceImpl(
        MeasurementRepository measurementRepository,
        MeasurementPointRepository measurementPointRepository,
        VehicleAmountService vehicleAmountService,
        SpeedDataService speedDataService
    ) {
        this.measurementRepository = measurementRepository;
        this.measurementPointRepository = measurementPointRepository;
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
        getAllHistoricalVehicleData(duration, LocalDateTime.now());
        //TODO, get data from cache table
        /*
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Pair<LocalDateTime, LocalDateTime>> timeSpans = new ArrayList<>();
        ArrayList<MeasurementStats> historicMeasurements = new ArrayList<>();
        ArrayList<Future<MeasurementStats>> futureHistoricMeasurements = new ArrayList<>();


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
            futureMeasurementStatss.add(buildHistoricMeasurementPart(
                ts.second(),
                ts.first(),
                ordinal,
                measurementPointId
            ));
            ordinal++;
        }

        for (Future<MeasurementStats> fhm : futureHistoricMeasurements) {
            try {
                historicMeasurements.add(fhm.get());
            } catch (CancellationException | InterruptedException | ExecutionException e) {
                historicMeasurements.add(new MeasurementStats("", 0, LocalDateTime.now(), 0, 0));
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
    public HashMap<LocalDateTime, ArrayList<MeasurementStats>> getAllHistoricalVehicleData(String duration, LocalDateTime now) {

        ArrayList<Pair<LocalDateTime, LocalDateTime>> timeSpans = new ArrayList<>();
        HashMap<LocalDateTime, ArrayList<MeasurementStats>> historicMeasurements = new HashMap<>();
        ArrayList<Future<MeasurementStats>> futureHistoricMeasurements = new ArrayList<>();

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
                buildMeasurementStatsPart(
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
     * @return List of MeasurementStats for every MeasurementPoint in the database in the requested timespan
     */
    public ArrayList<MeasurementStats> buildMeasurementStatsPart(LocalDateTime start, LocalDateTime end) {

        ArrayList<MeasurementStats> historicMeasurements = new ArrayList<>();

        List<Tuple> avgSpeedList = measurementRepository.findAverageVehicleSpeedByTimeBetween(start, end);
        List<Tuple> avgAmountList = measurementRepository.findSumVehicleAmountByTimeBetween(start, end);

        for (Tuple ta : avgAmountList) {
            String taId = (String) ta.get(0);
            Integer amount = (Integer) ta.get(1);

            Optional<Tuple> optionalSpeedTuple = avgSpeedList.stream().filter(s -> s.get(0).equals(taId)).findFirst();
            Double speed = optionalSpeedTuple.isPresent() ? (Double) optionalSpeedTuple.get().get(1) : 0d;

            historicMeasurements.add(
                new MeasurementStats(
                    taId, start, speed, amount, MeasurementStatsType.DAILY //TODO adjust type
                )
            );
        }

        return historicMeasurements;
    }

    //TODO, Schedule houry, best at exact hour XX:00
    private void aggregateVehicleDataHourly() {
        HashMap<LocalDateTime, ArrayList<MeasurementStats>> hourlyHistoricData = getAllHistoricalVehicleData("24h", LocalDateTime.now());

        //TODO, persist data

    }

    //TODO, Schedule daily, best at begin of day 00:00
    private void aggregateVehicleDataDaily() {
        HashMap<LocalDateTime, ArrayList<MeasurementStats>> dailyHistoricData = getAllHistoricalVehicleData("7d", LocalDateTime.now());

        //TODO, persist data
    }

}
