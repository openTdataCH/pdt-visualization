package ch.bfh.trafficcounter.model.dto.geojson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Properties class for static GeoJSON
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonPropertiesDto {

	private String id;

	private SpeedDataDto speedData;

	// TODO, add vehicle amount

	public GeoJsonPropertiesDto(String id) {
		this.id = id;
	}

}
