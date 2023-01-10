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
 * Represents vehicle amount.
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = @Index(columnList = "measurement_point_id"))
public class VehicleAmount {

    /**
     * Technical ID of vehicle amount.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Measurement point of vehicle amount.
     */
    @ManyToOne
    private MeasurementPoint measurementPoint;

    /**
     * Measurement.
     */
    @ManyToOne
    private Measurement measurement;

    /**
     * Vehicle amount measured.
     */
    @Column(nullable = false, updatable = false)
    private Integer numberOfVehicles;
}
