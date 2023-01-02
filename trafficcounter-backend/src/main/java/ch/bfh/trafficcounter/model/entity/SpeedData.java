/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.model.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Represents speed measurement data.
 *
 * @author Manuel Riesen
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = @Index(columnList = "measurement_point_id"))
public class SpeedData {

    /**
     * Technical ID of speed data.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Measurement point of the measured data.
     */
    @ManyToOne
    private MeasurementPoint measurementPoint;

    /**
     * Measurement.
     */
    @ManyToOne
    private Measurement measurement;

    /**
     * Average speed measured.
     */
    @Column(nullable = false, updatable = false)
    private Float averageSpeed;
}
