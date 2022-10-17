package ch.bfh.trafficcounter.model.dto.geojson;

public class GeoJsonGeometryDto {

	private static final String type = "Point";

	private Double[] coordinates;

	public GeoJsonGeometryDto(Double[] coordinates) {
		this.coordinates = coordinates;
	}

	public String getType() {
		return type;
	}

	public Double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Double[] coordinates) {
		this.coordinates = coordinates;
	}
}
