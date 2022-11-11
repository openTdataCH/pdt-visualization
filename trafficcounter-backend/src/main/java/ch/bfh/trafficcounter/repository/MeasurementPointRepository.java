package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementPointRepository extends JpaRepository<MeasurementPoint, String> {

	List<MeasurementPoint> findAllByActive(boolean active);

	int countAllByActive(boolean active);
}
