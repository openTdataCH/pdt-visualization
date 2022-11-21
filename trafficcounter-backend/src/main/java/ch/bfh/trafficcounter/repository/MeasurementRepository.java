package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.Measurement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for measurements.
 * @see Measurement
 *
 * @author Manuel Riesen
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    /**
     * Finds a measurement by its time.
     * @param time time to search for
     * @return measurement at the given time
     */
    Optional<Measurement> findByTime(LocalDateTime time);

    /**
     * Finds measurements and orders by time descending.
     * @param pageable pageable for more specific queries
     * @return measurements by time descending
     */
    @Query("SELECT m FROM Measurement m " +
            "JOIN FETCH m.speedData " +
            "ORDER BY m.time DESC")
    List<Measurement> findTimeDesc(Pageable pageable);
}
