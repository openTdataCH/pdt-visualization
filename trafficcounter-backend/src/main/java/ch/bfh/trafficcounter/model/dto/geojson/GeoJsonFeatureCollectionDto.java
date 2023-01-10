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

import java.util.List;

/**
 * Feature collection, wrapper around GeoJSON-Features
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
public class GeoJsonFeatureCollectionDto extends TypedGeoJsonDto {

    private List<GeoJsonFeatureDto> features;

    public GeoJsonFeatureCollectionDto() {
        super("FeatureCollection");
    }

    public GeoJsonFeatureCollectionDto(List<GeoJsonFeatureDto> features) {
        this();
        this.features = features;
    }
}
