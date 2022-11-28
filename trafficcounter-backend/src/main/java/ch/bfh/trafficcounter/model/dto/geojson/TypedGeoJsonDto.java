package ch.bfh.trafficcounter.model.dto.geojson;

/**
 * Represents a GeoJson object containing a type.
 *
 * @author Manuel Riesen
 */
public abstract class TypedGeoJsonDto {
    private final String type;

    public TypedGeoJsonDto(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
