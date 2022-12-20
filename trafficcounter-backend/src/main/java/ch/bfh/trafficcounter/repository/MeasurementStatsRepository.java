package ch.bfh.trafficcounter.repository;

import ch.bfh.trafficcounter.model.entity.MeasurementStats;
import ch.bfh.trafficcounter.model.entity.MeasurementStatsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Repository for measurement stats
 *
 * @author Manuel Riesen
 * @see MeasurementStats
 */
@Repository
public interface MeasurementStatsRepository extends JpaRepository<MeasurementStats, Long> {

    /**
     * Finds a List of MeasurementStats
     * @param measurementPointId the id of the measurement point
     * @param type the type
     * @param start the start time for the search
     * @param end the end tiem for the search
     * @return a list of MeasurementStats
     */
    ArrayList<MeasurementStats> findMeasurementStatsByMeasurementPointIdAndTypeAndTimeBetween(String measurementPointId, MeasurementStatsType type, LocalDateTime start, LocalDateTime end);

    /**
     * Finds all MeasurementStats of a type
     * @param type the type to search for
     * @return a list of all found MeasurementStats
     */
    ArrayList<MeasurementStats> findAllByType(MeasurementStatsType type);

    /**
     * Checks whether measurement stats for a measurementpoint exist
     * @param measurementPointId the id of the measurementpoint
     * @param deprecated whether the stats are deprecated or not
     * @return if a record matching the search-criteria is found
     */
    Boolean existsMeasurementStatsByMeasurementPointIdAndDeprecated(String measurementPointId, boolean deprecated);

    /**
     * Deletes all MeasurementType-Records by type and the field deprecated
     * @param type the type to search for
     * @param deprecated the deprecated-status to search for
     */
    void deleteAllByTypeAndDeprecated(MeasurementStatsType type, boolean deprecated);

}
