package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.HistoricMeasurement;
import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.Measurement;
import ch.bfh.trafficcounter.model.entity.SpeedData;
import ch.bfh.trafficcounter.model.entity.VehicleAmount;
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
import java.util.List;
import java.util.Optional;
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
        ArrayList<List<Measurement>> historicalData = new ArrayList<>();
        ArrayList<Pair<LocalDateTime, LocalDateTime>> timeSpans = new ArrayList<>();

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
            historicalData.add(measurementRepository.findAllByTimeBetween(ts.second(), ts.first()));
        }
        if (historicalData.isEmpty()) {
            return null;
        }

        ArrayList<HistoricMeasurement> historicalMeasurements = new ArrayList<>();

        int ordinal = 1;
        for (List<Measurement> m : historicalData) {
            LocalDateTime time = timeSpans.get(ordinal - 1).first();
            if (m.isEmpty()) {
                historicalMeasurements.add(new HistoricMeasurement(ordinal, time, 0, 0));
                ordinal++;
                continue;
            }
            historicalMeasurements.add(aggregateVehicleDataForMeasurementPoint(m, measurementPointId, ordinal, time));
            ordinal++;
        }

        return dtoMapper.mapHistoricVehicleDataToHistoricDataDto(historicalMeasurements, duration.substring(duration.length() - 1));
    }

    @Override
    public boolean hasHistoricData(String id) {
        return measurementPointRepository.existsMeasurementPointById(id);
    }


    /**
     * Aggregate data for a time period
     *
     * @param historicalData     a list of historical measurements
     * @param measurementPointId the id of the measurement point to aggregate data for
     * @param ordinal            the number indicating the order of the measurement in the period
     * @return a historic measurement
     */
    private HistoricMeasurement aggregateVehicleDataForMeasurementPoint(List<Measurement> historicalData, String measurementPointId, int ordinal, LocalDateTime time) {

        double speed = 0;
        int amount = 0;

        if (historicalData.size() < 1) {
            return null;
        }

        for (Measurement m : historicalData) {
            speed += m.getSpeedData().parallelStream().filter(mp -> mp.getMeasurementPoint().getId().equals(measurementPointId)).mapToDouble(SpeedData::getAverageSpeed).average().orElse(Double.NaN);
            amount += m.getVehicleAmounts().parallelStream().filter(mp -> mp.getMeasurementPoint().getId().equals(measurementPointId)).mapToInt(VehicleAmount::getNumberOfVehicles).average().orElse(Double.NaN);
        }

        double avgSpeed = speed / historicalData.size();
        int avgAmount = amount / historicalData.size();

        return new HistoricMeasurement(ordinal, time, avgSpeed, avgAmount);
    }

}
