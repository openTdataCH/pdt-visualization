package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.opentdata.wsdl.D2LogicalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MeasurementPointService {

	@Autowired
	private OpenTransportDataApiService api;


	@PostConstruct
	public void init() {
		D2LogicalModel response = api.pullMeasurementSiteTable();
		System.out.println();
	}

}
