package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.opentdata.wsdl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class DataRequestServiceImpl implements DataRequestService {

	private final OpenTransportDataApiService api;
	private final MeasurementPointService measurementPointService;

	private final SpeedDataService speedDataService;

	@Autowired
	public DataRequestServiceImpl(
			OpenTransportDataApiService api,
			MeasurementPointService measurementPointService,
			SpeedDataService speedDataService
	) {
		this.api = api;
		this.measurementPointService = measurementPointService;
		this.speedDataService = speedDataService;
	}

	@PostConstruct
	public void loadInitialData() {

		// load initial data on startup
		requestAndPersistStaticData();
	}


	/**
	 * implementation of static data request and persistence
	 * only enables valid measurement points (with coordinates)
	 *
	 */
	@Scheduled(
		cron = "${trafficcounter.schedules.static-data.cron}",
		zone = "${trafficcounter.schedules.static-data.zone}"
	)
	public void requestAndPersistStaticData() {
		final D2LogicalModel staticData = api.pullMeasurementSiteTable();
		final PayloadPublication payloadPublication = staticData.getPayloadPublication();
		if (!(payloadPublication instanceof final MeasurementSiteTablePublication mSTP)) {
			throw new ClassCastException("Expected MeasurementSiteTablePublication, but was not");
		}
		measurementPointService.processAndPersistMeasurementPoints(mSTP.getMeasurementSiteTable().get(0).getMeasurementSiteRecord());
		System.out.println("-- Successfully requested and persisted static data --");
	}

	@Scheduled(fixedRateString = "${trafficcounter.schedules.dynamic-data.rate}")
	public void requestAndPersistDynamicData() {
		final D2LogicalModel measuredData = api.pullMeasuredData();

		final PayloadPublication payloadPublication = measuredData.getPayloadPublication();
		if (!(payloadPublication instanceof final MeasuredDataPublication measuredDataPublication)) {
			throw new ClassCastException("Expected MeasuredDataPublication, but was not");
		}

		final LocalDateTime time = measuredDataPublication.getPublicationTime().toGregorianCalendar().toZonedDateTime().toLocalDateTime();

		final List<SiteMeasurements> siteMeasurements = measuredDataPublication.getSiteMeasurements();

		speedDataService.processAndPersistSpeedData(time, siteMeasurements);
		System.out.println("-- Successfully requested and persisted dynamic data --");
	}

}
