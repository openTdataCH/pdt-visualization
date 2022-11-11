package ch.bfh.trafficcounter.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A single Measurement Point with coordinates
 * can be enabled and disabled
 *
 * @author Sven Trachsel
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MeasurementPoint {

	@Id
	private String id;

	@Column(nullable = false, updatable = false)
	private double latitude;

	@Column(nullable = false, updatable = false)
	private double longtitude;

	@Column(nullable = false, updatable = false)
	private int numberOfLanes;

	@Column(nullable = false, updatable = false)
	private boolean active;

}
