package ch.bfh.trafficcounter.model.dto.geojson;

import lombok.Getter;
import lombok.Setter;

/**
 * Geometry class for static GeoJSON
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
public class GeoJsonGeometryDto extends TypedGeoJsonDto {

	private double[] coordinates;

	public GeoJsonGeometryDto() {
		super("Point");
	}

	public GeoJsonGeometryDto(double[] coordinates) {
		this();
		this.coordinates = coordinates;
	}
}
