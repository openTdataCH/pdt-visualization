package ch.bfh.trafficcounter.model.dto.geojson;

import java.util.ArrayList;
import lombok.*;

/**
 * Featurecollection, wrapper around GeoJSON-Features
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@AllArgsConstructor
public class GeoJsonFeatureCollectionDto {

	private static final String type = "FeatureCollection";
	private ArrayList<GeoJsonFeatureDto> features;

	public String getType() {
		return type;
	}

}
