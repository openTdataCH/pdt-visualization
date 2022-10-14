package ch.bfh.trafficcounter.model.dto.geojson;

import java.util.ArrayList;

public class GeoJsonFeatureCollectionDto {

	private final String type = "FeatureCollection";

	private ArrayList<GeoJsonFeatureDto> features;

}
