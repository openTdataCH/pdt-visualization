package ch.bfh.trafficcounter.mapper;

import ch.bfh.trafficcounter.config.SpeedDisplayConfig;
import ch.bfh.trafficcounter.model.HistoricMeasurement;
import ch.bfh.trafficcounter.model.dto.HistoricDataCollectionDto;
import ch.bfh.trafficcounter.model.dto.HistoricDataDto;
import ch.bfh.trafficcounter.model.dto.geojson.*;
import ch.bfh.trafficcounter.model.entity.MeasurementPoint;
import ch.bfh.trafficcounter.model.entity.SpeedData;
import ch.bfh.trafficcounter.model.entity.VehicleAmount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
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
    public GeoJsonFeatureCollectionDto mapVehicleAmountToGeoJsonFeatureCollectionDto(Collection<VehicleAmount> vehicleAmounts) {

        // check for empty array
        if (vehicleAmounts == null || vehicleAmounts.size() == 0) {
            return null;
        }

        return new GeoJsonFeatureCollectionDto(vehicleAmounts.stream()
            .map(data -> {
                final GeoJsonFeatureDto geoJsonFeatureDto = mapMeasurementPointToGeoJsonFeatureDto(data.getMeasurementPoint());
                final VehicleAmountDto vehicleAmountDto = new VehicleAmountDto(data.getNumberOfVehicles());
                geoJsonFeatureDto.getProperties().setVehicleAmount(vehicleAmountDto);
                return geoJsonFeatureDto;
            })
            .collect(Collectors.toList()));
    }

    @Override
    public GeoJsonFeatureCollectionDto mapVehicleDataToGeoJsonFeatureCollectionDto(Collection<VehicleAmount> vehicleAmounts, Collection<SpeedData> speedData) {

        if (vehicleAmounts == null || vehicleAmounts.size() == 0 || speedData == null || speedData.size() == 0) {
            return null;
        }

        final float maxSpeed = speedData.stream().max(Comparator.comparing(SpeedData::getAverageSpeed))
            .map(SpeedData::getAverageSpeed).orElse(0f);

        List<GeoJsonFeatureDto> speedDataGeoJsonFeatureDtosList = speedData.stream()
            .map(vehicleSpeeds -> {
                final GeoJsonFeatureDto geoJsonFeatureDto = mapMeasurementPointToGeoJsonFeatureDto(vehicleSpeeds.getMeasurementPoint());
                final SpeedDataDto speedDataDto = new SpeedDataDto(
                    vehicleSpeeds.getAverageSpeed(),
                    speedDisplayConfig.getSpeedDisplayClass(maxSpeed > 0 ? vehicleSpeeds.getAverageSpeed() / maxSpeed : 0)
                );
                geoJsonFeatureDto.getProperties().setSpeedData(speedDataDto);
                return geoJsonFeatureDto;
            }).toList();

        // only contains speed data
        ArrayList<GeoJsonFeatureDto> geoJsonFeatureDtos = new ArrayList<>(speedDataGeoJsonFeatureDtosList);

        // add vehicle amount data
        for (GeoJsonFeatureDto geoJsonFeatureDto :
            geoJsonFeatureDtos) {

            Optional<VehicleAmount> vehicleAmount = vehicleAmounts.stream().filter(amount -> amount.getMeasurementPoint().getId().equals(geoJsonFeatureDto.getProperties().getId())).findFirst();

            VehicleAmountDto vehicleAmountDto = new VehicleAmountDto();
            if (vehicleAmount.isEmpty()) {
                System.out.printf("No vehicleamount for speeddata with id %s found", geoJsonFeatureDto.getProperties().getId());
                continue;
            }
            vehicleAmountDto.setNumberOfVehicles(vehicleAmount.get().getNumberOfVehicles());

            geoJsonFeatureDto.getProperties().setVehicleAmount(vehicleAmountDto);

        }

        return new GeoJsonFeatureCollectionDto(geoJsonFeatureDtos);
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

    @Override
    public HistoricDataCollectionDto mapHistoricVehicleDataToHistoricDataDto(List<HistoricMeasurement> measurements, String resolution) {

        HistoricDataDto[] measurementsArray = new HistoricDataDto[measurements.size()];

        int cnt = 0;
        for (HistoricMeasurement h : measurements) {
            HistoricDataDto historicDataDto = new HistoricDataDto(h.getOrdinal(),h.getAvgVehicleAmount(), h.getAvgVehicleSpeed());
            measurementsArray[cnt] = historicDataDto;
            cnt++;
        }

        return new HistoricDataCollectionDto(resolution, measurementsArray);
    }

    // following methods are not used from outside and are therefore not part of interface. private for cleanliness
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
