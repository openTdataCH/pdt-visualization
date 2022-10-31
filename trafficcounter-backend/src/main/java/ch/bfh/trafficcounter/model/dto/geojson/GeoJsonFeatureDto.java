package ch.bfh.trafficcounter.model.dto.geojson;
import lombok.*;

/**
 * Features class for static GeoJSON, containing geometry and properties
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonFeatureDto {

	private String type = "Feature";
	private GeoJsonGeometryDto geometry;
	private GeoJsonPropertiesDto properties;

	public GeoJsonFeatureDto(GeoJsonGeometryDto geometry, GeoJsonPropertiesDto properties) {
		this.geometry = geometry;
		this.properties = properties;
	}
}
