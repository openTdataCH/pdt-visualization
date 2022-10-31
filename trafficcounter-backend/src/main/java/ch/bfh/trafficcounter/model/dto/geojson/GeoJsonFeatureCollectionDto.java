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
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonFeatureCollectionDto {

	private String type = "FeatureCollection";
	private ArrayList<GeoJsonFeatureDto> features;

	public GeoJsonFeatureCollectionDto(ArrayList<GeoJsonFeatureDto> features) {
		this.features = features;
	}
}
