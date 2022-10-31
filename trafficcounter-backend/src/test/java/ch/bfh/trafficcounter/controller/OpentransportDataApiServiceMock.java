package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.opentdata.wsdl.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@Primary
public class OpentransportDataApiServiceMock implements OpenTransportDataApiService {

	@Override
	public D2LogicalModel pullMeasuredData() {
		return null;
	}

	@Override
	public D2LogicalModel pullMeasurementSiteTable() {
		D2LogicalModel model = new D2LogicalModel();
		MeasurementSiteTablePublication measurementSiteTablePublication = new MeasurementSiteTablePublication() {{
			measurementSiteTable = List.of(new MeasurementSiteTable() {{
				MeasurementSiteRecord record = new MeasurementSiteRecord();
				record.setId("CH-TEST");
				MultilingualString measurementSiteName = new MultilingualString();
				MultilingualStringValue measurementSiteNameValue = new MultilingualStringValue();
				measurementSiteNameValue.setValue("Test-Ort");
				MultilingualString.Values values = new MultilingualString.Values() {{
					value = List.of(measurementSiteNameValue);
				}};
				measurementSiteName.setValues(values);
				record.setMeasurementSiteName(measurementSiteName);

				Point point = new Point();
				PointByCoordinates pointByCoordinates = new PointByCoordinates();
				PointCoordinates pointCoordinates = new PointCoordinates();
				pointCoordinates.setLatitude(1);
				pointCoordinates.setLongitude(2);
				pointByCoordinates.setPointCoordinates(pointCoordinates);
				point.setPointByCoordinates(pointByCoordinates);

				record.setMeasurementSiteLocation(point);

				record.setMeasurementSiteNumberOfLanes(BigInteger.ONE);
				measurementSiteRecord = List.of(record);
			}});
		}};
		measurementSiteTablePublication.getMeasurementSiteTable().get(0).setId("TEST");
		model.setPayloadPublication(measurementSiteTablePublication);
		return model;
	}
}
