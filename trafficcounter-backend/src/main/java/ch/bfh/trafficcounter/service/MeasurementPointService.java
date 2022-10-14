package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import ch.opentdata.wsdl.D2LogicalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MeasurementPointService {

	@Autowired
	private OpenTransportDataApiService api;


	public GeoJsonFeatureCollectionDto getAllMeasurementPointsGeoJson() {
		//TODO, get data from repositories and put into geojsondtos
		return null;
	}

	@PostConstruct
	public void init() {
		//TODO, request data and save into database
		//D2LogicalModel response = api.pullMeasurementSiteTable();
		//System.out.println();
		//Meas
	}

}
