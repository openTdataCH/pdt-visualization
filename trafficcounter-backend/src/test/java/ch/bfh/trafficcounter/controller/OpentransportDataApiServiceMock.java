package ch.bfh.trafficcounter.controller;

import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.opentdata.wsdl.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.when;

@Service
@Profile("test")
@Primary
public class OpentransportDataApiServiceMock implements OpenTransportDataApiService {

    private final MeasurementPointRepository measurementPointRepository;

    @Autowired
    public OpentransportDataApiServiceMock(MeasurementPointRepository measurementPointRepository) {
        this.measurementPointRepository = measurementPointRepository;
    }

    @Override
    public D2LogicalModel pullMeasuredData() {

        final String measurementPointId = "CH-TEST";
        if (measurementPointRepository.findById(measurementPointId).isEmpty()) {
            measurementPointRepository.save(MeasurementPoint.builder()
                .id(measurementPointId)
                .active(true)
                .latitude(1.0)
                .longtitude(1.0)
                .numberOfLanes(1)
                .build());
        }

        final D2LogicalModel d2LogicalModel = new D2LogicalModel();
        final MeasuredDataPublication measuredDataPublication = new MeasuredDataPublication() {{
            siteMeasurements = List.of(new SiteMeasurements() {{
                measurementSiteReference = new MeasurementSiteRecordVersionedReference();
                measurementSiteReference.setId(measurementPointId);
                final SiteMeasurementsIndexMeasuredValue siteMeasurementsIndexMeasuredValue = new SiteMeasurementsIndexMeasuredValue();
                siteMeasurementsIndexMeasuredValue.setIndex(1);
                final MeasuredValue value = new MeasuredValue();
                final TrafficSpeed trafficSpeed = new TrafficSpeed();
                final SpeedValue speedValue = new SpeedValue();
                speedValue.setNumberOfInputValuesUsed(BigInteger.valueOf(1));
                speedValue.setSpeed(10f);
                trafficSpeed.setAverageVehicleSpeed(speedValue);
                value.setBasicData(trafficSpeed);
                siteMeasurementsIndexMeasuredValue.setMeasuredValue(value);
                measuredValue = List.of(siteMeasurementsIndexMeasuredValue);
            }});
        }};

        final XMLGregorianCalendar xmlGregorianCalendar = Mockito.mock(XMLGregorianCalendar.class);
        when(xmlGregorianCalendar.toGregorianCalendar()).thenReturn(GregorianCalendar.from(ZonedDateTime.now()));
        measuredDataPublication.setPublicationTime(xmlGregorianCalendar);
        d2LogicalModel.setPayloadPublication(measuredDataPublication);
        return d2LogicalModel;
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
