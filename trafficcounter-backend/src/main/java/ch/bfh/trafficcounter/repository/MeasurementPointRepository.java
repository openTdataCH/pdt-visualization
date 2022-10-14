package ch.bfh.trafficcounter.repository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeasurementPointRepository extends JpaRepository<Module, UUID>{

}
