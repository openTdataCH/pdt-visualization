/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonGeometryDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonPropertiesDto;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MeasurementPointControllerTest extends AbstractApiTest {

    @Test
    void getMeasurementPointsGeoJson() {
        GeoJsonFeatureCollectionDto featureCollection = request()
            .get("/api/measurementpoints")
            .then()
            .assertThat()
            .body("features.size()", is(1))
            .statusCode(HttpStatus.SC_OK)
            .extract().body().as(GeoJsonFeatureCollectionDto.class);

        assertEquals("FeatureCollection", featureCollection.getType());

        List<GeoJsonFeatureDto> features = featureCollection.getFeatures();
        assertEquals(1, features.size());

        GeoJsonFeatureDto feature = features.get(0);
        assertEquals("Feature", feature.getType());

        GeoJsonGeometryDto geometry = feature.getGeometry();
        assertEquals("Point", geometry.getType());
        assertEquals(2.0, geometry.getCoordinates()[0]);
        assertEquals(1.0, geometry.getCoordinates()[1]);

        GeoJsonPropertiesDto properties = feature.getProperties();
        assertEquals("CH-TEST", properties.getId());
    }
}
