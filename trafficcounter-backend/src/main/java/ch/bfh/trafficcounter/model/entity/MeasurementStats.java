/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Represents measurement statistics.
 *
 * @author Manuel Riesen
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementStats {

    /**
     * Technical ID of measurement statistics.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * ID of measurement point.
     */
    private String measurementPointId;

    /**
     * Time of aggregation (begin).
     */
    private LocalDateTime time;

    /**
     * Average vehicle speed.
     */
    private double avgVehicleSpeed;

    /**
     * Sum of vehicle amounts.
     */
    private int sumVehicleAmount;

    /**
     * Type of measurement statistics.
     */
    private MeasurementStatsType type;

    /**
     * Whether the statistics data is deprecated.
     */
    private boolean deprecated;

    public MeasurementStats(String measurementPointId, LocalDateTime time, double avgVehicleSpeed, int sumVehicleAmount, MeasurementStatsType type) {
        this.measurementPointId = measurementPointId;
        this.time = time;
        this.avgVehicleSpeed = avgVehicleSpeed;
        this.sumVehicleAmount = sumVehicleAmount;
        this.type = type;
    }
}
