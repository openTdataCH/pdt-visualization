package ch.bfh.trafficcounter.repository.result;

/**
 * Represents the result of a speed data aggregation.
 *
 * @author Manuel Riesen
 */
public interface SpeedDataAggregationResult extends HistoricalDataAggregationResult {

    /**
     * Gets the average vehicle speed.
     *
     * @return average vehicle speed
     */
    Float getAverageVehicleSpeed();
}
