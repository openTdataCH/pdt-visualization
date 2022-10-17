package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.opentdata.wsdl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Manages querying the api and saving the data to the database
 */
@Service
@EnableScheduling
public class DataRequestService {

	private final OpenTransportDataApiService api;
	private final MeasurementPointRepository mPRepo;

	private boolean firstStart = true;

	@Autowired
	public DataRequestService(
			OpenTransportDataApiService api,
			MeasurementPointRepository mPRepo
	) {
		this.api = api;
		this.mPRepo = mPRepo;
	}

	@Scheduled(fixedRate = 3600000) // do it every hour, starting immediately
	public void requestAndPersistData() {
		if (firstStart) {
			firstStart = false;
			requestAndPersistStaticData();
		}
		requestAndPersistDynamicData();
	}

	private void requestAndPersistStaticData() {
		ArrayList<MeasurementPoint> measurementPoints = new ArrayList<>();
		D2LogicalModel staticData = api.pullMeasurementSiteTable();
		PayloadPublication payloadPublication = staticData.getPayloadPublication();
		if (!(payloadPublication instanceof final MeasurementSiteTablePublication mSTP)) {
			throw new ClassCastException("Expected MeasurementSiteTablePublication, but was not");
		}

		for (MeasurementSiteRecord mSRecord : mSTP.getMeasurementSiteTable().get(0).getMeasurementSiteRecord()) {

			if (!(mSRecord.getMeasurementSiteLocation() instanceof final Point pt)) {
				throw new ClassCastException("Expected Point, but was not");
			}

			// extract coordinates
			PointByCoordinates ptByCoords = pt.getPointByCoordinates();
			if (ptByCoords == null) {
				System.out.println(String.format("Skipping site %s, no coordinates", mSRecord.getId()));
				continue;
			}

			double[] coordinates = {ptByCoords.getPointCoordinates().getLatitude(), ptByCoords.getPointCoordinates().getLongitude()};

			measurementPoints.add(
				MeasurementPoint.builder()
					.id(mSRecord.getId())
					.latitude(coordinates[0])
					.longtitude(coordinates[1])
					.numberOfLanes(mSRecord.getMeasurementSiteNumberOfLanes().intValue())
					.active(true)
					.build()
			);
		}
		mPRepo.saveAll(measurementPoints);
	}

	private void requestAndPersistDynamicData() {
		//TODO, use once dynamic data is used
	}

}
