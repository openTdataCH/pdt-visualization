package ch.bfh.trafficcounter.model.dto.geojson;

public class GeoJsonFeatureDto {

	private static final String type = "Feature";

	private GeoJsonGeometryDto geometry;

	private GeoJsonPropertiesDto properties;

	public GeoJsonGeometryDto getGeometry() {
		return geometry;
	}

	public void setGeometry(GeoJsonGeometryDto geometry) {
		this.geometry = geometry;
	}
}
