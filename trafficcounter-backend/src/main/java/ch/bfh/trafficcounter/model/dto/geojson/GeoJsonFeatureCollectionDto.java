package ch.bfh.trafficcounter.model.dto.geojson;

import java.util.ArrayList;

public class GeoJsonFeatureCollectionDto {

	private static final String type = "FeatureCollection";

	private ArrayList<GeoJsonFeatureDto> features;

	public ArrayList<GeoJsonFeatureDto> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<GeoJsonFeatureDto> features) {
		this.features = features;
	}
}
