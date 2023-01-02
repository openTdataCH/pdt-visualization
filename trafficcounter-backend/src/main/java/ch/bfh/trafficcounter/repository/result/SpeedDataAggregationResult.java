/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

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
