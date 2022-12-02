package ch.bfh.trafficcounter.controller;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VehicleDataControllerTest extends AbstractApiTest {

	@Test
	void testGetVehicleAmount() {
		request()
				.get("/api/vehicledata")
				.then()
				.assertThat()
				.statusCode(HttpStatus.SC_OK);
	}

}
