/*
 * Copyright 2023 Manuel Riesen, Sandro Rüfenacht, Sven Trachsel
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.HistoricDataDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonGeometryDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonPropertiesDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.MeasurementStats;
import ch.bfh.trafficcounter.model.entity.MeasurementStatsType;
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

    @Override
    public HistoricDataCollectionDto mapHistoricVehicleDataToHistoricDataDto(List<MeasurementStats> measurements, MeasurementStatsType type) {

        if (measurements.size() == 0) {
            return null;
        }

        HistoricDataDto[] measurementsArray = new HistoricDataDto[measurements.size()];

        int cnt = 0;
        for (MeasurementStats h : measurements) {
            HistoricDataDto historicDataDto = new HistoricDataDto(h.getTime(), h.getSumVehicleAmount(), h.getAvgVehicleSpeed());
            measurementsArray[cnt] = historicDataDto;
            cnt++;
        }

        return new HistoricDataCollectionDto(type.getDuration(), measurementsArray);
    }

    @Override
    public GeoJsonFeatureDto mapMeasurementPointToGeoJsonFeatureDto(MeasurementPoint mp) {
        return new GeoJsonFeatureDto(mapMeasurementPointToGeoJsonGeometryDto(mp), mapMeasurementPointToGeoJsonPropertiesDto(mp));
    }

    // following methods are not used from outside and are therefore not part of interface. private for cleanliness
    private GeoJsonGeometryDto mapMeasurementPointToGeoJsonGeometryDto(MeasurementPoint mp) {
        return new GeoJsonGeometryDto(new double[]{mp.getLongtitude(), mp.getLatitude()});
    }

    private GeoJsonPropertiesDto mapMeasurementPointToGeoJsonPropertiesDto(MeasurementPoint mp) {
        return new GeoJsonPropertiesDto(mp.getId());
    }
}
