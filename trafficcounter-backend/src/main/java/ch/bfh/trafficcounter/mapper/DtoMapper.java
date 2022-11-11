package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;

import java.util.List;

/**
 * Mapper for different DTOs
 *
 * @author Sven Trachsel
 */
public interface DtoMapper {
	GeoJsonFeatureCollectionDto mapMeasurementPointsToGeoJsonFeatureCollectionDto(List<MeasurementPoint> measurementPoint);

}
