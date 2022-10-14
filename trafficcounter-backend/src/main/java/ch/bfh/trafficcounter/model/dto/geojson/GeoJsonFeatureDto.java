package ch.bfh.trafficcounter.model.dto.geojson;

public class GeoJsonFeatureDto {

	private final String type = "Feature";

	private GeoJsonGeometryDto geometry;

	private GeoJsonPropertiesDto properties;
}
