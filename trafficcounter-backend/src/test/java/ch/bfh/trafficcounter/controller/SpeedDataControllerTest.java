package ch.bfh.trafficcounter.controller;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SpeedDataControllerTest extends AbstractApiTest {

	@Test
    void testGetSpeedData() {
        request()
			.get("/api/speeddata")
			.then()
			.assertThat()
			.body("size()", is(1))
			.body("[0].speed", equalTo(10f))
			.statusCode(HttpStatus.SC_OK);
    }
}
