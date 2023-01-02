/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.model.dto.geojson;

import lombok.Getter;
import lombok.Setter;

/**
 * Features class for static GeoJSON, containing geometry and properties
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
public class GeoJsonFeatureDto extends TypedGeoJsonDto {

    private GeoJsonGeometryDto geometry;
    private GeoJsonPropertiesDto properties;

    public GeoJsonFeatureDto() {
        super("Feature");
    }

    public GeoJsonFeatureDto(GeoJsonGeometryDto geometry, GeoJsonPropertiesDto properties) {
        this();
        this.geometry = geometry;
        this.properties = properties;
    }
}
