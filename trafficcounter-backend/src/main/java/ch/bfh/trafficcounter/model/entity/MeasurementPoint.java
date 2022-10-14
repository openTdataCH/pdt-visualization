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
}
