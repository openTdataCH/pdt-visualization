package ch.bfh.trafficcounter.controller;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeasurementPointControllerTest extends AbstractApiTest {

	@Test
	@DirtiesContext
	void getMeasurementPointsGeoJson() {
		request()
				.get("/api/measurementpoints")
				.then()
				.assertThat()
				.body("features.size()", is(1))
				.statusCode(HttpStatus.SC_OK);
		//TODO add more validation
	}
}
