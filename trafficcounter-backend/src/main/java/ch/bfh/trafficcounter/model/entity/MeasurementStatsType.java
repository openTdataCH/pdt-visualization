/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.model.entity;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents a type of measurement statistics.
 *
 * @author Manuel Riesen
 */
public enum MeasurementStatsType {

    /**
     * Data is aggregated over the last seven days in steps of days.
     */
    DAILY("7d"),

    /**
     * Data is aggregated over the last 24 hours in steps of hours.
     */
    HOURLY("24h");

    private final String duration;

    MeasurementStatsType(String duration) {
        this.duration = duration;
    }

    public static Optional<MeasurementStatsType> fromDuration(final String duration) {
        return Arrays.stream(values()).filter(type -> type.getDuration().equals(duration)).findFirst();
    }

    public String getDuration() {
        return duration;
    }
}
