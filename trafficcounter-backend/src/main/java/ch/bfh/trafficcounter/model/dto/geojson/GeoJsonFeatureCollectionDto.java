package ch.bfh.trafficcounter.model.dto.geojson;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Feature collection, wrapper around GeoJSON-Features
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
public class GeoJsonFeatureCollectionDto extends TypedGeoJsonDto {

    private List<GeoJsonFeatureDto> features;

    public GeoJsonFeatureCollectionDto() {
        super("FeatureCollection");
    }

    public GeoJsonFeatureCollectionDto(List<GeoJsonFeatureDto> features) {
        this();
        this.features = features;
    }
}
