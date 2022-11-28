package ch.bfh.trafficcounter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class TrafficcounterApplicationTests {

	public TrafficcounterApplicationTests() {
		// set protocolHandler to custom handler for WSDL
		System.setProperty("java.protocol.handler.pkgs", "ch.bfh.trafficcounter.protocols");
	}

	@Test
	void contextLoads() {
	}

}
