package ch.bfh.trafficcounter.model.dto.geojson;

import lombok.*;

/**
 * Geometry class for static GeoJSON
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonGeometryDto {

	private String type = "Point";
	private Double[] coordinates;

	public GeoJsonGeometryDto(Double[] coordinates) {
		this.coordinates = coordinates;
	}
}
