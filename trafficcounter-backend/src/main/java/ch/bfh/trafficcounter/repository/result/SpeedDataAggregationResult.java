package ch.bfh.trafficcounter.repository.result;

public interface SpeedDataAggregationResult extends HistoricalDataAggregationResult {

    Float getAverageVehicleSpeed();
}
