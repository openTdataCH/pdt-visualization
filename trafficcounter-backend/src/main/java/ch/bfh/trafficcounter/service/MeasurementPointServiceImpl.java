package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for Measurement points
 *
 * @author Sven Trachsel
 */
@Service
public class MeasurementPointServiceImpl implements MeasurementPointService {

	private final MeasurementPointRepository measurementPointRepository;
	private final DtoMapper dtoMapper;

	@Autowired
	public MeasurementPointServiceImpl(DtoMapper dtoMapper, MeasurementPointRepository measurementPointRepository) {
		this.measurementPointRepository = measurementPointRepository;
		this.dtoMapper = dtoMapper;
	}

	@Override
	public GeoJsonFeatureCollectionDto getAllMeasurementPointsGeoJson() {
		final List<MeasurementPoint> measurementPoints = measurementPointRepository.findAllByActive(true);
		return dtoMapper.mapMeasurementPointsToGeoJsonFeatureCollectionDto(measurementPoints);
	}

	@Override
	public int getNumberOfMeasurementPoints() {
		return measurementPointRepository.countAllByActive(true);
	}

}
