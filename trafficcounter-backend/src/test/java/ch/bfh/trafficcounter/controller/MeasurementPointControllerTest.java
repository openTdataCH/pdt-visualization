package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonGeometryDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonPropertiesDto;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeasurementPointControllerTest extends AbstractApiTest {

	@Test
	@DirtiesContext
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
