package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonGeometryDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonPropertiesDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link DtoMapper}
 *
 * @author Sven Trachsel
 */
@Component
public class DtoMapperImpl implements DtoMapper {

	/**
	 * Wraps measurementPoints into GeoJSON
	 *
	 * @param measurementPoints an arraylist of measurement-point objects to wrap in GeoJSON
	 * @return a DTO object which can easily be serialized to GeoJSON
	 */
	@Override
	public GeoJsonFeatureCollectionDto mapMeasurementPointsToGeoJsonFeatureCollectionDto(List<MeasurementPoint> measurementPoints) {
		// check for empty array
		if (measurementPoints == null || measurementPoints.size() == 0) {
			return null;
		}

		final List<GeoJsonFeatureDto> featureDtos = new ArrayList<>(measurementPoints.size());
		for (MeasurementPoint mp : measurementPoints) {
			featureDtos.add(mapMeasurementPointToGeoJsonFeatureDto(mp));
		}

		return new GeoJsonFeatureCollectionDto(featureDtos);
	}

	// following methos are not used from outside and are therefore not part of interface. private for cleanliness
	private GeoJsonFeatureDto mapMeasurementPointToGeoJsonFeatureDto(MeasurementPoint mp) {
		return new GeoJsonFeatureDto(mapMeasurementPointToGeoJsonGeometryDto(mp), mapMeasurementPointToGeoJsonPropertiesDto(mp));
	}

	private GeoJsonGeometryDto mapMeasurementPointToGeoJsonGeometryDto(MeasurementPoint mp) {
		return new GeoJsonGeometryDto(new double[]{mp.getLongtitude(), mp.getLatitude()});
	}

	private GeoJsonPropertiesDto mapMeasurementPointToGeoJsonPropertiesDto(MeasurementPoint mp) {
		return new GeoJsonPropertiesDto(mp.getId());
	}
}
