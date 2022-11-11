package ch.bfh.trafficcounter.model.dto.geojson;

import lombok.Getter;
import lombok.Setter;

/**
 * Features class for static GeoJSON, containing geometry and properties
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
public class GeoJsonFeatureDto extends TypedGeoJsonDto {

	private GeoJsonGeometryDto geometry;
	private GeoJsonPropertiesDto properties;

	public GeoJsonFeatureDto() {
		super("Feature");
	}

	public GeoJsonFeatureDto(GeoJsonGeometryDto geometry, GeoJsonPropertiesDto properties) {
		this();
		this.geometry = geometry;
		this.properties = properties;
	}
}
