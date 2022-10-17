package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import ch.bfh.trafficcounter.service.api.OpenTransportDataApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Service
public class MeasurementPointService {

	@Autowired
	private OpenTransportDataApiService api;

	//private final MeasurementPointRepository measurementPointRepository;
	private final DtoMapper dtoMapper;

	@Autowired
	public MeasurementPointService(DtoMapper dtoMapper){
		//public MeasurementPointService(MeasurementPointRepository measurementPointRepository, DtoMapper dtoMapper){
		//this.measurementPointRepository = measurementPointRepository;
		this.dtoMapper = dtoMapper;
	}


	public GeoJsonFeatureCollectionDto getAllMeasurementPointsGeoJson() {

		ArrayList<MeasurementPoint> measurementPoints = new ArrayList<>(); //TODO, get data from repository
		return dtoMapper.mapMeasurementPointsToGeoJsonFeatureCollectionDto(measurementPoints);
	}

	@PostConstruct
	public void init() {
		//ArrayList<MeasurementPoint> measurementPoints;
		//D2LogicalModel staticData = api.pullMeasurementSiteTable();

		//staticData.getPayloadPublication();

		//measurementPointRepository.saveAll(measurementPoints);


		//TODO, request data and save into database
		//D2LogicalModel response = api.pullMeasurementSiteTable();
		System.out.println();
		//Meas
	}

}
