package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.opentdata.wsdl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class DataRequestServiceImpl implements DataRequestService {

	private final OpenTransportDataApiService api;
	private final MeasurementPointRepository mPRepo;

	private boolean firstStart = true;

	@Autowired
	public DataRequestServiceImpl(
			OpenTransportDataApiService api,
			MeasurementPointRepository mPRepo
	) {
		this.api = api;
		this.mPRepo = mPRepo;
	}

	/**
	 * Scheduled task to request data every hour.
	 */
	@Scheduled(fixedRate = 3600000) // do it every hour, starting immediately
	public void requestAndPersistData() {
		if (firstStart) {
			firstStart = false;
			requestAndPersistStaticData();
		}
		requestAndPersistDynamicData();
	}

	/**
	 * implementation of static data request and persistence
	 * only enables valid measurement points (with coordinates)
	 *
	 */
	private void requestAndPersistStaticData() {
		final List<MeasurementPoint> measurementPoints = new ArrayList<>();
		final D2LogicalModel staticData = api.pullMeasurementSiteTable();
		final PayloadPublication payloadPublication = staticData.getPayloadPublication();
		if (!(payloadPublication instanceof final MeasurementSiteTablePublication mSTP)) {
			throw new ClassCastException("Expected MeasurementSiteTablePublication, but was not");
		}

		for (MeasurementSiteRecord mSRecord : mSTP.getMeasurementSiteTable().get(0).getMeasurementSiteRecord()) {
			boolean isActive = false;
			if (!(mSRecord.getMeasurementSiteLocation() instanceof final Point pt)) {
				throw new ClassCastException("Expected Point, but was not");
			}


			final double[] coordinates = {0, 0};
			// extract coordinates
			final PointByCoordinates ptByCoords = pt.getPointByCoordinates();
			if (ptByCoords != null) {
				isActive = true;
				coordinates[0] = ptByCoords.getPointCoordinates().getLatitude();
				coordinates[1] = ptByCoords.getPointCoordinates().getLongitude();
			}
			measurementPoints.add(
					MeasurementPoint.builder()
							.id(mSRecord.getId())
							.latitude(coordinates[0])
							.longtitude(coordinates[1])
							.numberOfLanes(mSRecord.getMeasurementSiteNumberOfLanes().intValue())
							.active(isActive)
							.build()
			);
		}
		mPRepo.saveAll(measurementPoints);
		System.out.println("-- Successfully requested and persisted static data --");
	}

	private void requestAndPersistDynamicData() {
		//TODO, use once dynamic data is used
	}

}
