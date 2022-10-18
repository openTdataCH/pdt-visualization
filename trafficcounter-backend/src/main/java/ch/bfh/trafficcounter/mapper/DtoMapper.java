package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonGeometryDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonPropertiesDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;

import java.util.ArrayList;

public interface DtoMapper {
	GeoJsonFeatureCollectionDto mapMeasurementPointsToGeoJsonFeatureCollectionDto(ArrayList<MeasurementPoint> measurementPoint);

}
