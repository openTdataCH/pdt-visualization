package ch.bfh.trafficcounter.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MeasurementPoint {

	@Id
	private String id;

	private double latitude;

	private double longtitude;

	private int numberOfLanes;

	private boolean active;

}
