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
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Represents a measurement of a specific time.
 *
 * @author Manuel Riesen
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = @Index(columnList = "time"))
public class Measurement {

    /**
     * Technical ID of the measurement.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Time of measurement.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime time;

    /**
     * Speed data measured at the time.
     */
    @OneToMany(mappedBy = "measurement")
    private Set<SpeedData> speedData;

    /**
     * Amount of vehicles measured at the time.
     */
    @OneToMany(mappedBy = "measurement")
    private Set<VehicleAmount> vehicleAmounts;

}
