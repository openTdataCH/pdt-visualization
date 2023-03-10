/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.event.UpdateEvent;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.opentdata.wsdl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link DataRequestService}.
 *
 * @author Sven Trachsel
 * @author Manuel Riesen
 */
@Service
@Transactional
public class DataRequestServiceImpl implements DataRequestService {

    private final OpenTransportDataApiService api;

    private final MeasurementPointService measurementPointService;

    private final SpeedDataService speedDataService;

    private final VehicleAmountService vehicleAmountService;

    private final VehicleDataService vehicleDataService;

    private final Sinks.Many<UpdateEvent> updateEvent;

    @Autowired
    public DataRequestServiceImpl(
        OpenTransportDataApiService api,
        MeasurementPointService measurementPointService,
        SpeedDataService speedDataService,
        VehicleDataService vehicleDataService,
        VehicleAmountService vehicleAmountService, Sinks.Many<UpdateEvent> updateEvent) {
        this.api = api;
        this.measurementPointService = measurementPointService;
        this.speedDataService = speedDataService;
        this.vehicleAmountService = vehicleAmountService;
        this.vehicleDataService = vehicleDataService;
        this.updateEvent = updateEvent;
    }

    /**
     * initialization of first static and then dynamic data
     * fills the measurementStats for the first time after data is requested
     */
    @PostConstruct
    public void loadInitialData() {

        // load initial data on startup
        requestAndPersistStaticData();
        requestAndPersistDynamicData();

        vehicleDataService.initializeAggregatedData();
    }


    /**
     * implementation of static data request and persistence
     * only enables valid measurement points (with coordinates)
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

    /**
     * implementation of dynamic data requests and persistence
     * runs once a minute
     */
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
        System.out.println("-- Successfully requested and persisted speed data --");

        vehicleAmountService.processAndPersistVehicleAmount(time, siteMeasurements);
        System.out.println("-- Successfully requested and persisted amount of vehicles --");

        speedDataService.updateEstimatedSpeedLimit();
        System.out.println("-- Successfully recalculated estimated speed limit --");

        updateEvent.tryEmitNext(UpdateEvent.ALL);
    }

}
