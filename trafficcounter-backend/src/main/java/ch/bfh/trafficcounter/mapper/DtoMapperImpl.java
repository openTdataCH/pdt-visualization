package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.config.SpeedDisplayConfig;
import ch.bfh.trafficcounter.model.dto.geojson.*;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.SpeedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DtoMapper}
 *
 * @author Sven Trachsel
 */
@Component
public class DtoMapperImpl implements DtoMapper {

    private final SpeedDisplayConfig speedDisplayConfig;

    @Autowired
    public DtoMapperImpl(SpeedDisplayConfig speedDisplayConfig) {
        this.speedDisplayConfig = speedDisplayConfig;
    }

    @Override
    public GeoJsonFeatureCollectionDto mapSpeedDataToGeoJsonFeatureCollectionDto(final Collection<SpeedData> speedData) {

        // check for empty array
        if (speedData == null || speedData.size() == 0) {
            return null;
        }

        final float maxSpeed = speedData.stream().max(Comparator.comparing(SpeedData::getAverageSpeed))
                .map(SpeedData::getAverageSpeed).orElse(0f);

        return new GeoJsonFeatureCollectionDto(speedData.stream()
                .map(data -> {
                    final GeoJsonFeatureDto geoJsonFeatureDto = mapMeasurementPointToGeoJsonFeatureDto(data.getMeasurementPoint());
                    final SpeedDataDto speedDataDto = new SpeedDataDto(
                            data.getAverageSpeed(),
                            speedDisplayConfig.getSpeedDisplayClass(maxSpeed > 0 ? data.getAverageSpeed() / maxSpeed : 0)
                    );
                    geoJsonFeatureDto.getProperties().setSpeedData(speedDataDto);
                    return geoJsonFeatureDto;
                })
                .collect(Collectors.toList()));
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
