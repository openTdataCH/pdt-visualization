package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.MeasurementStats;
import ch.bfh.trafficcounter.model.entity.MeasurementStatsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public interface MeasurementStatsRepository extends JpaRepository<MeasurementStats, Long> {

    ArrayList<MeasurementStats> findMeasurementStatsByMeasurementPointIdAndTypeAndTimeBetween(String measurementPointId, MeasurementStatsType type, LocalDateTime start, LocalDateTime end);

    Boolean existsMeasurementStatsByMeasurementPointId(String measurementPointId);

}
