package ch.bfh.trafficcounter.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

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
