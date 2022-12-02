package ch.bfh.trafficcounter.controller;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VehicleDataControllerTest extends AbstractApiTest {

	@Test
	void testGetVehicleAmount() {
		request()
				.get("/api/vehicledata")
				.then()
				.assertThat()
				.body("features.size()", is(1))
				.body("features[0].properties.speedData.averageSpeed", equalTo(10f))
				.body("features[0].properties.speedData.speedDisplayClass", equalTo("high"))
				.body("features[0].properties.vehicleAmount.numberOfVehicles", equalTo(1))
				.statusCode(HttpStatus.SC_OK);
	}

}
