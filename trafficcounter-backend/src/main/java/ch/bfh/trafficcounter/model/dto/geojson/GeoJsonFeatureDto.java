package ch.bfh.trafficcounter.model.dto.geojson;

public class GeoJsonFeatureDto {

	private static final String type = "Feature";

	private GeoJsonGeometryDto geometry;

	private GeoJsonPropertiesDto properties;

	public GeoJsonFeatureDto(GeoJsonGeometryDto geometry, GeoJsonPropertiesDto properties) {
		this.geometry = geometry;
		this.properties = properties;
	}

	public GeoJsonGeometryDto getGeometry() {
		return geometry;
	}

	public void setGeometry(GeoJsonGeometryDto geometry) {
		this.geometry = geometry;
	}

	public GeoJsonPropertiesDto getProperties() {
		return properties;
	}

	public void setProperties(GeoJsonPropertiesDto properties) {
		this.properties = properties;
	}

	public String getType() {
		return type;
	}
}
