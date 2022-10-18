package ch.bfh.trafficcounter.service.api;

import ch.opentdata.wsdl.D2LogicalModel;

public interface OpenTransportDataApiService {

    D2LogicalModel pullMeasuredData();

    D2LogicalModel pullMeasurementSiteTable();
}
