package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementPointRepository extends JpaRepository<MeasurementPoint, String> {

}
