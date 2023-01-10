/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

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

    @Query(value = "UPDATE measurement_point mp " +
        "SET mp.estimated_speed_limit = (SELECT ROUND(AVG(sd.average_speed) / 10, 0) * 10 FROM speed_data sd " +
        "JOIN measurement_point mp ON mp.id = sd.measurement_point_id " +
        "JOIN measurement m ON sd.measurement_id = m.id " +
        "WHERE m.time > :endTime)",
        nativeQuery = true
    )
    @Modifying
    void updateEstimatedSpeedLimit(@Param("endTime") LocalDateTime endTime);

}
