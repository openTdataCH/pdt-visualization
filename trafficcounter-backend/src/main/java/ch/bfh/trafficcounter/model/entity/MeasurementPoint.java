package ch.bfh.trafficcounter.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MeasurementPoint {

	@Id
	private String id;

	private double latitude;

	private double longtitude;

	private int numberOfLanes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
	}
}
