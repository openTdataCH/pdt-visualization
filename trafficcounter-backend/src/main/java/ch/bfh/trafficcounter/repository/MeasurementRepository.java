package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.Measurement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    Optional<Measurement> findByTime(LocalDateTime time);

    @Query("SELECT m FROM Measurement m " +
            "JOIN FETCH m.speedData " +
            "ORDER BY m.time DESC")
    List<Measurement> findTimeDesc(Pageable pageable);
}
