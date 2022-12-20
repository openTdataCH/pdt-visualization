package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.MeasurementStats;
import ch.bfh.trafficcounter.model.entity.MeasurementStatsType;

import java.util.List;

/**
 * Mapper for different DTOs
 *
 * @author Sven Trachsel
 */
public interface DtoMapper {


    /**
     * Wraps measurementPoints into GeoJSON
     *
     * @param measurementPoints an arraylist of measurement-point objects to wrap in GeoJSON
     * @return a DTO object which can easily be serialized to GeoJSON
     */
    GeoJsonFeatureCollectionDto mapMeasurementPointsToGeoJsonFeatureCollectionDto(List<MeasurementPoint> measurementPoints);

    /**
     * Wraps historic measurements into a JSON
     *
     * @param measurements List of historic measurements for each measurement time implied by resolution
     * @param type         given resolution type, implies amount of measurements (daily -> 7, hourly -> 24)
     * @return a DTO object for transmitting historic data
     */
    HistoricDataCollectionDto mapHistoricVehicleDataToHistoricDataDto(List<MeasurementStats> measurements, MeasurementStatsType type);

    GeoJsonFeatureDto mapMeasurementPointToGeoJsonFeatureDto(MeasurementPoint measurementPoint);
}
