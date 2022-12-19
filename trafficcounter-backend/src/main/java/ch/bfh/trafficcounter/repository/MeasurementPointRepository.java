package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for measurement point entities.
 *
 * @author Sven Trachsel
 * @see MeasurementPoint
 */
@Repository
public interface MeasurementPointRepository extends JpaRepository<MeasurementPoint, String> {

    /**
     * Finds all measurement points by their active state.
     *
     * @param active active state
     * @return measurement points matching active state
     */
    List<MeasurementPoint> findAllByActive(boolean active);

    /**
     * Counts all measurement points by their active state.
     *
     * @param active active state
     * @return number of measurement points
     */
    int countAllByActive(boolean active);

    /**
     * Returns if measurement point is found
     *
     * @param id id of the measurement point
     * @return true if found, false if not
     */
    Boolean existsMeasurementPointById(String id);

    @Query("UPDATE MeasurementPoint mp " +
        "SET mp.estimatedSpeedLimit = (SELECT ROUND(AVG(sd.averageSpeed) / 10, 0) * 10 FROM SpeedData sd WHERE sd.measurementPoint.id = mp.id AND sd.measurement.time > :endTime)")
    @Modifying
    void updateEstimatedSpeedLimit(@Param("endTime") LocalDateTime endTime);

}
