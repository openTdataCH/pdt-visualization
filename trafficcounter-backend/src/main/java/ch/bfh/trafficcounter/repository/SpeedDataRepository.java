package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.SpeedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for speed data.
 * @see SpeedData
 *
 * @author Manuel Riesen
 */
@Repository
public interface SpeedDataRepository extends JpaRepository<SpeedData, Long> {
}
