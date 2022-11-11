package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.model.dto.geojson.*;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.SpeedData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DtoMapper}
 *
 * @author Sven Trachsel
 */
@Component
public class DtoMapperImpl implements DtoMapper {

	@Override
	public List<SpeedDataDto> mapSpeedDataToSpeedDataDto(final Collection<SpeedData> speedData) {
		return speedData.stream()
				.map(value -> new SpeedDataDto(value.getMeasurementPoint().getId(), value.getAverageSpeed()))
				.collect(Collectors.toList());
	}

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
