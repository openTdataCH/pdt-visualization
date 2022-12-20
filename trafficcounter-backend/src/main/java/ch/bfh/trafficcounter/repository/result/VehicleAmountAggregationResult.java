package ch.bfh.trafficcounter.repository.result;

/**
 * Represents the result of a vehicle amount aggregation.
 *
 * @author Manuel Riesen
 */
public interface VehicleAmountAggregationResult extends HistoricalDataAggregationResult {

    /**
     * Gets the sum of the vehicle amounts.
     * @return sum of vehicle amounts
     */
    Integer getSumVehicleAmount();

}
