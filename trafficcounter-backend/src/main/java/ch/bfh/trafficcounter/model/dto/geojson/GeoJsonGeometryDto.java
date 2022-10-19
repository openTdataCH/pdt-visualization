package ch.bfh.trafficcounter.model.dto.geojson;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GeoJsonGeometryDto {

	private static final String type = "Point";

	private Double[] coordinates;

	public String getType() {
		return type;
	}

}
