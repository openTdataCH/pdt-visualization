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
 * Properties class for static GeoJSON
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonPropertiesDto {

    private String id;

    private SpeedDataDto speedData;

    private VehicleAmountDto vehicleAmount;

    public GeoJsonPropertiesDto(String id) {
        this.id = id;
    }

}
