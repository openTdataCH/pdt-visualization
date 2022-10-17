package ch.bfh.trafficcounter.model.dto.geojson;

public class GeoJsonPropertiesDto {

	private int year;

	public GeoJsonPropertiesDto(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
