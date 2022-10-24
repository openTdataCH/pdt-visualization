package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;

import java.util.ArrayList;

/**
 * Mapper for different DTOs
 *
 * @author Sven Trachsel
 */
public interface DtoMapper {
	GeoJsonFeatureCollectionDto mapMeasurementPointsToGeoJsonFeatureCollectionDto(ArrayList<MeasurementPoint> measurementPoint);

}
