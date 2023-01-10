/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

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
