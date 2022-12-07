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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


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

        LocalDateTime start;
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Optional<List<Measurement>>> historicalData = new ArrayList<>();
        Map<LocalDateTime, LocalDateTime> timeSpans = new HashMap<>();

        switch (duration) {
            case "24h":
                for (long i = 1L; i <= 24L; i++) {
                    timeSpans.put(now.minusHours(i), now.minusHours(i - 1));
                }
                break;
            case "7d":
                for (long i = 1L; i <= 7L; i++) {
                    timeSpans.put(now.minusDays(i), now.minusDays(i - 1));
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsupported duration: %s", duration));
        }

        // gets data either hourly 24x or daily 7x
        for (Map.Entry<LocalDateTime, LocalDateTime> ts : timeSpans.entrySet()) {
            historicalData.add(measurementRepository.findAllByTimeBetween(ts.getKey(), ts.getValue()));
        }
        if (historicalData.isEmpty()) {
            return null;
        }

        ArrayList<HistoricMeasurement> historicalMeasurements = new ArrayList<>();

        int ordinal = 1;
        for (Optional<List<Measurement>> m : historicalData) {
            if (m.isEmpty()) {
                continue;
            }
            historicalMeasurements.add(aggregateVehicleDataForMeasurementPoint(m.get(), measurementPointId, ordinal));
            ordinal++;
        }

        return dtoMapper.mapHistoricVehicleDataToHistoricDataDto(historicalMeasurements, duration.substring(duration.length() - 2));
    }

    /**
     * Aggregate data for a time period
     *
     * @param historicalData a list of historical measurements
     * @param measurementPointId the id of the measurement point to aggregate data for
     * @param ordinal the number indicating the order of the measurement in the period
     * @return a historic measurement
     */
    private HistoricMeasurement aggregateVehicleDataForMeasurementPoint(List<Measurement> historicalData, String measurementPointId, int ordinal) {

        double speed = 0;
        int amount = 0;

        for (Measurement m : historicalData) {
            speed += m.getSpeedData().stream().filter(mp -> mp.getMeasurementPoint().getId().equals(measurementPointId)).mapToDouble(SpeedData::getAverageSpeed).average().orElse(Double.NaN);
            amount += m.getVehicleAmounts().stream().filter(mp -> mp.getMeasurementPoint().getId().equals(measurementPointId)).mapToInt(VehicleAmount::getNumberOfVehicles).average().orElse(Double.NaN);
        }

        double avgSpeed = speed / historicalData.size();
        int avgAmount = amount / historicalData.size();

        return new HistoricMeasurement(ordinal, avgSpeed, avgAmount);
    }

}
