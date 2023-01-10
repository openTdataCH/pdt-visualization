/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.model.dto.geojson;

/**
 * Represents a GeoJson object containing a type.
 *
 * @author Manuel Riesen
 */
public abstract class TypedGeoJsonDto {
    private final String type;

    public TypedGeoJsonDto(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
