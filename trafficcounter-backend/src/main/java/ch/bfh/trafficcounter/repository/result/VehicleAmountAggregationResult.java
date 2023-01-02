/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.repository.result;

/**
 * Represents the result of a vehicle amount aggregation.
 *
 * @author Manuel Riesen
 */
public interface VehicleAmountAggregationResult extends HistoricalDataAggregationResult {

    /**
     * Gets the sum of the vehicle amounts.
     *
     * @return sum of vehicle amounts
     */
    Integer getSumVehicleAmount();

}
