package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.model.HistoricMeasurement;
import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.geojson.GeoJsonFeatureCollectionDto;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.SpeedData;
import ch.bfh.trafficcounter.model.entity.VehicleAmount;

import java.util.Collection;
import java.util.List;

/**
 * Mapper for different DTOs
 *
 * @author Sven Trachsel
 */
public interface DtoMapper {


    /**
     * Creates GeoJson DTOs from speed data entities.
     *
     * @param speedData speed data entities
     * @return DTO containing speed data
     */
    GeoJsonFeatureCollectionDto mapSpeedDataToGeoJsonFeatureCollectionDto(Collection<SpeedData> speedData);

    /**
     * Creates GeoJson DTOs from vehicle amount entities.
     *
     * @param vehicleAmounts vehicleAmount entities
     * @return DTO containing vehicle amounts
     */
    GeoJsonFeatureCollectionDto mapVehicleAmountToGeoJsonFeatureCollectionDto(Collection<VehicleAmount> vehicleAmounts);

    /**
     * Creates GeoJson DTOs from vehicle amount and speed data entities.
     *
     * @param vehicleAmounts vehicleAmount entities
     * @param speedData      speed data entities
     * @return DTO containing vehicle amounts
     */
    GeoJsonFeatureCollectionDto mapVehicleDataToGeoJsonFeatureCollectionDto(Collection<VehicleAmount> vehicleAmounts, Collection<SpeedData> speedData);

    /**
     * Wraps measurementPoints into GeoJSON
     *
     * @param measurementPoints an arraylist of measurement-point objects to wrap in GeoJSON
     * @return a DTO object which can easily be serialized to GeoJSON
     */
    GeoJsonFeatureCollectionDto mapMeasurementPointsToGeoJsonFeatureCollectionDto(List<MeasurementPoint> measurementPoints);

    /**
     * Wraps historic measurements into a Json
     *
     * @param measurements List of historic measurements for each measurement time implied by resolution
     * @param resolution   given resolution, implies amount of measurements (daily -> 7, hourly -> 24)
     * @return a DTO object for transmitting historic data
     */
    HistoricDataCollectionDto mapHistoricVehicleDataToHistoricDataDto(List<HistoricMeasurement> measurements, String resolution);

}
