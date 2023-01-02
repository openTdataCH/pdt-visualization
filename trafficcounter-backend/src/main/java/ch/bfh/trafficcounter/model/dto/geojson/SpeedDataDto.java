/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.model.dto.geojson;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents speed data of a measurement point.
 *
 * @author Manuel Riesen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpeedDataDto {

    /**
     * Average speed.
     */
    private float averageSpeed;

    /**
     * Estimated speed limit.
     */
    private Float estimatedSpeedLimit;

    /**
     * Speed display class name.
     *
     * @see SpeedDisplayClass
     */
    private String speedDisplayClass;

}
