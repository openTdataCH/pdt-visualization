package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.MeasurementStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementStatsRepository extends JpaRepository<MeasurementStats, Long> {


}
