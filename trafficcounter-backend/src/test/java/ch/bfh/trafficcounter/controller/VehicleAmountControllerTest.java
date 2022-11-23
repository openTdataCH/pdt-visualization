package ch.bfh.trafficcounter.controller;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.equalTo;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VehicleAmountControllerTest extends AbstractApiTest {

	@Test
	void testGetVehicleAmount() {
		request()
				.get("/api/vehicleamount")
				.then()
				.assertThat()
				.body("features[0].properties.vehicleAmount.numberOfVehicles", equalTo(1))
				.statusCode(HttpStatus.SC_OK);
	}

}
