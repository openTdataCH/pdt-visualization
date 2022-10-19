package ch.bfh.trafficcounter.model.dto.geojson;
import lombok.*;

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
