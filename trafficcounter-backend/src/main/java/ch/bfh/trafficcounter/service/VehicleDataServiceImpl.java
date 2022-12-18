package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.model.entity.MeasurementStats;
import ch.bfh.trafficcounter.model.entity.MeasurementStatsType;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.repository.MeasurementRepository;
import ch.bfh.trafficcounter.repository.MeasurementStatsRepository;
import org.glassfish.pfl.basic.contain.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    private final MeasurementStatsRepository measurementStatsRepository;

    private final DtoMapper dtoMapper;

    @Autowired
    public VehicleDataServiceImpl(
        MeasurementRepository measurementRepository,
        MeasurementPointRepository measurementPointRepository,
        VehicleAmountService vehicleAmountService,
        SpeedDataService speedDataService,
        MeasurementStatsRepository measurementStatsRepository,
        DtoMapper dtoMapper
    ) {
        this.measurementRepository = measurementRepository;
        this.measurementPointRepository = measurementPointRepository;
        this.vehicleAmountService = vehicleAmountService;
        this.speedDataService = speedDataService;
        this.measurementStatsRepository = measurementStatsRepository;
        this.dtoMapper = dtoMapper;
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

        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        MeasurementStatsType type;
        LocalDateTime start;


        switch (duration) {
            case "24h" -> {
                type = MeasurementStatsType.HOURLY;
                start = now.minusHours(24);
            }
            case "7d" -> {
                type = MeasurementStatsType.DAILY;
                now = now.withHour(0); // set to midnight for daily
                start = now.minusDays(7);
            }
            default -> throw new IllegalArgumentException(String.format("Unsupported duration: %s", duration));
        }

        ArrayList<MeasurementStats> historicMeasurements = measurementStatsRepository.findMeasurementStatsByMeasurementPointIdAndTypeAndTimeBetween(measurementPointId, type, start, now);

        return dtoMapper.mapHistoricVehicleDataToHistoricDataDto(historicMeasurements, type.toString());
    }

    /**
     * Aggregates a list of historic measurement for a given duration
     *
     * @param duration duration to aggregate over (24h or 7d)
     * @param now      the start time from where results are aggregated backwards
     * @return a map where the key is the start time of the HistoricData and the value is a List of all aggregation for every MeasurementPoint in the database
     * has 24 entries for 24h and 7 entries for 7d
     */
    public ArrayList<ArrayList<MeasurementStats>> getAllHistoricalVehicleData(String duration, LocalDateTime now) {

        ArrayList<Pair<LocalDateTime, LocalDateTime>> timeSpans = new ArrayList<>();
        ArrayList<ArrayList<MeasurementStats>> historicMeasurements = new ArrayList<>();
        MeasurementStatsType type;

        switch (duration) {
            case "24h":
                type = MeasurementStatsType.HOURLY;
                for (long i = 1L; i <= 24L; i++) {
                    timeSpans.add(new Pair<>(now.minusHours(i - 1), now.minusHours(i)));
                }
                break;
            case "7d":
                type = MeasurementStatsType.DAILY;
                for (long i = 1L; i <= 7L; i++) {
                    timeSpans.add(new Pair<>(now.minusDays(i - 1), now.minusDays(i)));
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported duration: %s", duration));
        }

        // gets data either hourly 24x or daily 7x
        for (Pair<LocalDateTime, LocalDateTime> ts : timeSpans) {
            historicMeasurements.add(
                buildMeasurementStatsPart(
                    ts.second(),
                    ts.first(),
                    type
                )
            );
        }
        return historicMeasurements;
    }

    @Override
    public boolean hasHistoricData(String id) {
        return measurementStatsRepository.existsMeasurementStatsByMeasurementPointIdAndDeprecated(id, false);
    }

    /**
     * Builds a list of all historic measurements in a given timespan. reads avg speed an amount data
     *
     * @param start start time for query
     * @param end   end time for query
     * @return List of MeasurementStats for every MeasurementPoint in the database in the requested timespan
     */
    public ArrayList<MeasurementStats> buildMeasurementStatsPart(LocalDateTime start, LocalDateTime end, MeasurementStatsType type) {

        ArrayList<MeasurementStats> historicMeasurements = new ArrayList<>();

        List<Tuple> avgSpeedList = measurementRepository.findAverageVehicleSpeedByTimeBetween(start, end);
        List<Tuple> avgAmountList = measurementRepository.findSumVehicleAmountByTimeBetween(start, end);

        for (Tuple ta : avgAmountList) {
            String taId = ta.get(0, String.class);
            Integer amount = ta.get(1, Integer.class);

            Optional<Tuple> optionalSpeedTuple = avgSpeedList.stream().filter(s -> s.get(0).equals(taId)).findFirst();
            Double speed = optionalSpeedTuple.isPresent() ? optionalSpeedTuple.get().get(1, Double.class) : 0d;

            historicMeasurements.add(
                new MeasurementStats(
                    taId, start, speed, amount, type
                )
            );
        }
        return historicMeasurements;
    }

    @Scheduled(
        cron = "${trafficcounter.schedules.hourly-aggregation.cron}",
        zone = "${trafficcounter.schedules.hourly-aggregation.zone}"
    )
    public void aggregateVehicleDataHourly() {
        System.out.println("-- Begin aggregating hourly data --");
        MeasurementStatsType type = MeasurementStatsType.HOURLY;
        markCurrentStatsDeprecated(type);
        ArrayList<ArrayList<MeasurementStats>> hourlyHistoricData = getAllHistoricalVehicleData("24h", LocalDateTime.now());
        hourlyHistoricData.forEach(measurementStatsRepository::saveAll);
        measurementStatsRepository.deleteAllByTypeAndDeprecated(type, true);
        System.out.println("-- Successfully aggregated and persisted hourly data --");
    }

    @Scheduled(
        cron = "${trafficcounter.schedules.daily-aggregation.cron}",
        zone = "${trafficcounter.schedules.daily-aggregation.zone}"
    )
    public void aggregateVehicleDataDaily() {
        System.out.println("-- Begin aggregating daily data --");
        MeasurementStatsType type = MeasurementStatsType.DAILY;
        markCurrentStatsDeprecated(type);
        ArrayList<ArrayList<MeasurementStats>> dailyHistoricData = getAllHistoricalVehicleData("7d", LocalDateTime.now());
        dailyHistoricData.forEach(measurementStatsRepository::saveAll);
        measurementStatsRepository.deleteAllByTypeAndDeprecated(type, true);
        System.out.println("-- Successfully aggregated and persisted daily data --");
    }

    private void markCurrentStatsDeprecated(MeasurementStatsType type) {
        ArrayList<MeasurementStats> measurementStats = measurementStatsRepository.findAllByType(type);
        measurementStats.forEach(s -> s.setDeprecated(true));
        measurementStatsRepository.saveAll(measurementStats);
    }

}
