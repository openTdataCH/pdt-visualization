package ch.bfh.trafficcounter.service;

import ch.bfh.trafficcounter.mapper.DtoMapper;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.repository.MeasurementPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

/**
 * Service for Measurement points
 *
 * @author Sven Trachsel
 */
@Service
public class MeasurementPointService {

	private final MeasurementPointRepository measurementPointRepository;
	private final DtoMapper dtoMapper;

	@Autowired
	public MeasurementPointService(DtoMapper dtoMapper, MeasurementPointRepository measurementPointRepository) {
		this.measurementPointRepository = measurementPointRepository;
		this.dtoMapper = dtoMapper;
	}

	/**
	 * Gets a GeoJSON-List of all registered Measurement Points
	 * Only finds active measurement points
	 * @return a DTO in GeoJSON format
	 */
	public GeoJsonFeatureCollectionDto getAllMeasurementPointsGeoJson() {
		ArrayList<MeasurementPoint> measurementPoints = measurementPointRepository.findAllByActive(true);
		return dtoMapper.mapMeasurementPointsToGeoJsonFeatureCollectionDto(measurementPoints);
	}

	/**
	 * Counts number of active measurement points, zero indicates that no data has been loaded yet
	 * @return number of active measurement points
	 */
	public int getNumberOfMeasurementPoints() {
		return measurementPointRepository.countAllByActive(true);
	}

}
