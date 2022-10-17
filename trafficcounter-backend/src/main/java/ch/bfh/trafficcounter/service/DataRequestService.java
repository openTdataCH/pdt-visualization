package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Manages querying the api and saving the data to the database
 */
public class DataRequestService {

	private OpenTransportDataApiService api;
	private final MeasurementPointRepository mpRepo;
	@Autowired
	public DataRequestService(
			OpenTransportDataApiService api,
			MeasurementPointRepository mpRepo
	) {
		this.api = api;
		this.mpRepo = mpRepo;
	}

	@PostConstruct
	public void init() {
		//TODO, request data and save into database


		//ArrayList<MeasurementPoint> measurementPoints;
		//D2LogicalModel staticData = api.pullMeasurementSiteTable();

		//staticData.getPayloadPublication();

		//measurementPointRepository.saveAll(measurementPoints);


		//D2LogicalModel response = api.pullMeasurementSiteTable();
		System.out.println();
		//Meas

	}

}
