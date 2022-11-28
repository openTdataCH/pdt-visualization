package ch.bfh.trafficcounter.mapper;

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
     * Wraps measurementPoints into GeoJSON
     *
     * @param measurementPoints an arraylist of measurement-point objects to wrap in GeoJSON
     * @return a DTO object which can easily be serialized to GeoJSON
     */
    GeoJsonFeatureCollectionDto mapMeasurementPointsToGeoJsonFeatureCollectionDto(List<MeasurementPoint> measurementPoints);

}
