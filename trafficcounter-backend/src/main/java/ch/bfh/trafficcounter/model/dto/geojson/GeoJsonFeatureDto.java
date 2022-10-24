package ch.bfh.trafficcounter.model.dto.geojson;
import lombok.*;

/**
 * Features class for static GeoJSON, containing geometry and properties
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@AllArgsConstructor
public class GeoJsonFeatureDto {

	private static final String type = "Feature";

	private GeoJsonGeometryDto geometry;

	private GeoJsonPropertiesDto properties;


	public String getType() {
		return type;
	}

}
