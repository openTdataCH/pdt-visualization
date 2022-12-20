package ch.bfh.trafficcounter.repository.result;

/**
 * Represents the result of historical data aggregation.
 *
 * @author Manuel Riesen
 */
public interface HistoricalDataAggregationResult {

    /**
     * Gets the measurement point ID.
     *
     * @return measurement point ID
     */
    String getMeasurementPointId();
}
