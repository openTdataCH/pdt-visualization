package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for measurements.
 *
 * @author Manuel Riesen
 * @see Measurement
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    /**
     * Finds a measurement by its time.
     *
     * @param time time to search for
     * @return measurement at the given time
     */
    Optional<Measurement> findByTime(LocalDateTime time);

    /**
     * Finds the latest measurement
     *
     * @return latest measurement
     */
    @Query("SELECT m FROM Measurement m " +
        "WHERE m.time = (SELECT MAX(m2.time) FROM Measurement m2)")
    Optional<Measurement> findLatest();

    /**
     * Finds all measurements in a timespan
     *
     * @param start start time for search
     * @param end   end time for search
     * @return a list of all found measurements in between start and end time
     * @author Sven Trachsel
     */
    List<Measurement> findAllByTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = """
        SELECT * FROM measurement m
        JOIN vehicle_amount v on m.id = v.measurement_id
        JOIN speed_data s on m.id = s.measurement_id
        WHERE v.measurement_point_id = :measurementPointId AND s.measurement_point_id = :measurementPointId
        AND time BETWEEN :start AND :end""", nativeQuery = true)
    List<Measurement> findAllByTimeBetweenAndMeasurementPointId(@Param("measurementPointId") String measurementPointId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = """
        SELECT AVG(v.number_of_vehicles) AS avgVehicleAmount FROM measurement m
        JOIN vehicle_amount v on m.id = v.measurement_id
        WHERE v.measurement_point_id = :measurementPointId
        AND time BETWEEN :start AND :end
        GROUP BY v.measurement_point_id""", nativeQuery = true)
    Integer findAverageVehicleAmountByTimeBetweenAndMeasurementPointId(@Param("measurementPointId") String measurementPointId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = """
        SELECT AVG(s.average_speed) avgVehicleSpeed FROM measurement m
        JOIN speed_data s on m.id = s.measurement_id
        WHERE s.measurement_point_id = :measurementPointId
        AND time BETWEEN :start AND :end
        GROUP BY s.measurement_point_id""", nativeQuery = true)
    Double findAverageVehicleSpeedByTimeBetweenAndMeasurementPointId(@Param("measurementPointId") String measurementPointId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);



    /**
     * Counts the number of records between two dates
     *
     * @param start start time for search
     * @param end   end time for search
     * @return number of records found
     * @author Sven Trachsel
     */
    Integer countAllByTimeIsBetween(LocalDateTime start, LocalDateTime end);
}
