package ch.bfh.trafficcounter.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractApiTest {

	@LocalServerPort
	protected int port;

	protected RequestSpecification request() {
		return RestAssured
				.given()
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.port(this.port)
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
	}

}
