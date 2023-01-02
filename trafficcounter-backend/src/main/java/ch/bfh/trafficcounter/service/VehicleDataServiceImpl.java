/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

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
import ch.bfh.trafficcounter.repository.result.SpeedDataAggregationResult;
import ch.bfh.trafficcounter.repository.result.VehicleAmountAggregationResult;
import org.glassfish.pfl.basic.contain.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public HistoricDataCollectionDto getHistoricalVehicleData(String measurementPointId, MeasurementStatsType type) {

        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime start;

        switch (type) {
            case HOURLY -> {
                start = now.minusHours(24);
            }
            case DAILY -> {
                now = now.withHour(0); // set to midnight for daily
                start = now.minusDays(7);
            }
            default -> throw new IllegalArgumentException(String.format("Unsupported duration: %s", type));
        }

        final List<MeasurementStats> historicMeasurements = measurementStatsRepository.findMeasurementStatsByMeasurementPointIdAndTypeAndTimeBetween(measurementPointId, type, start, now);

        return dtoMapper.mapHistoricVehicleDataToHistoricDataDto(historicMeasurements, type);
    }

    /**
     * Aggregates a list of historic measurement for a given duration
     *
     * @param now the start time from where results are aggregated backwards
     * @return a map where the key is the start time of the HistoricData and the value is a List of all aggregation for every MeasurementPoint in the database
     * has 24 entries for 24h and 7 entries for 7d
     */
    public List<List<MeasurementStats>> getAllHistoricalVehicleData(MeasurementStatsType type, LocalDateTime now) {

        final List<Pair<LocalDateTime, LocalDateTime>> timeSpans = new ArrayList<>();
        final List<List<MeasurementStats>> historicMeasurements = new ArrayList<>();

        switch (type) {
            case HOURLY:
                for (long i = 1L; i <= 24L; i++) {
                    timeSpans.add(new Pair<>(now.minusHours(i - 1), now.minusHours(i)));
                }
                break;
            case DAILY:
                for (long i = 1L; i <= 7L; i++) {
                    timeSpans.add(new Pair<>(now.minusDays(i - 1), now.minusDays(i)));
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported duration: %s", type));
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
    public List<MeasurementStats> buildMeasurementStatsPart(LocalDateTime start, LocalDateTime end, MeasurementStatsType type) {

        final List<SpeedDataAggregationResult> averageSpeedData = measurementRepository.findAverageVehicleSpeedByTimeBetween(start, end);
        final List<VehicleAmountAggregationResult> sumVehicleAmounts = measurementRepository.findSumVehicleAmountByTimeBetween(start, end);

        return Stream.concat(
            averageSpeedData.stream(),
            sumVehicleAmounts.stream()
        ).map(result -> {
            final MeasurementStats stats = new MeasurementStats();
            stats.setMeasurementPointId(result.getMeasurementPointId());
            stats.setType(type);
            stats.setTime(start);
            if (result instanceof SpeedDataAggregationResult) {
                stats.setAvgVehicleSpeed(((SpeedDataAggregationResult) result).getAverageVehicleSpeed());
            }
            if (result instanceof VehicleAmountAggregationResult) {
                stats.setSumVehicleAmount(((VehicleAmountAggregationResult) result).getSumVehicleAmount());
            }
            return stats;
        }).collect(Collectors.toMap(MeasurementStats::getMeasurementPointId, Function.identity(), (stats1, stats2) -> {

                if (stats1.getAvgVehicleSpeed() == 0) {
                    stats1.setAvgVehicleSpeed(stats2.getAvgVehicleSpeed());
                }
                if (stats1.getSumVehicleAmount() == 0) {
                    stats1.setSumVehicleAmount(stats2.getSumVehicleAmount());
                }
                return stats1;
            })
        ).values().stream().toList();
    }

    @Override
    public void initializeAggregatedData() {
        LocalDateTime startTime = LocalDateTime.now();
        aggregateVehicleData(MeasurementStatsType.HOURLY, startTime);
        aggregateVehicleData(MeasurementStatsType.DAILY, startTime);
    }

    @Scheduled(
        cron = "${trafficcounter.schedules.hourly-aggregation.cron}",
        zone = "${trafficcounter.schedules.hourly-aggregation.zone}")
    public void aggregateHourlyStats() {
        aggregateVehicleData(MeasurementStatsType.HOURLY, LocalDateTime.now());
    }

    @Scheduled(
        cron = "${trafficcounter.schedules.daily-aggregation.cron}",
        zone = "${trafficcounter.schedules.daily-aggregation.zone}")
    public void aggregateDailyStats() {
        aggregateVehicleData(MeasurementStatsType.DAILY, LocalDateTime.now());
    }

    private void aggregateVehicleData(MeasurementStatsType type, LocalDateTime startTime) {
        System.out.printf("-- Begin aggregating %s data --%n", type);
        markCurrentStatsDeprecated(type);
        List<List<MeasurementStats>> hourlyHistoricData = getAllHistoricalVehicleData(type, startTime);
        hourlyHistoricData.forEach(measurementStatsRepository::saveAll);
        measurementStatsRepository.deleteAllByTypeAndDeprecated(type, true);
        System.out.printf("-- Successfully aggregated and persisted %s data --%n", type);
    }

    private void markCurrentStatsDeprecated(MeasurementStatsType type) {
        ArrayList<MeasurementStats> measurementStats = measurementStatsRepository.findAllByType(type);
        measurementStats.forEach(s -> s.setDeprecated(true));
        measurementStatsRepository.saveAll(measurementStats);
    }
}
