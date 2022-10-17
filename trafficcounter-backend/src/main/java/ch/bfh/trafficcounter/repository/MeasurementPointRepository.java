package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MeasurementPointRepository extends JpaRepository<MeasurementPoint, String> {

	ArrayList<MeasurementPoint> findAllByActive(boolean active);
}
