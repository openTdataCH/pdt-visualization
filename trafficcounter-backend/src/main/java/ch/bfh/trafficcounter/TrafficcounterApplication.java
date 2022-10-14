package ch.bfh.trafficcounter;

import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiServiceImpl;
import ch.opentdata.wsdl.D2LogicalModel;
import ch.opentdata.wsdl.PullService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class TrafficcounterApplication {


	public static void main(String[] args) {
		SpringApplication.run(TrafficcounterApplication.class, args);
	}

	@Autowired
	private OpenTransportDataApiService api;

	/*
	@PostConstruct
	public void init() {
		D2LogicalModel response = api.pullMeasuredData();
		System.out.println();
	}
	 */

}
